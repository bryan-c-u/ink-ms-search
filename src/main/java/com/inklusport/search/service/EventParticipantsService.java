package com.inklusport.search.service;

import com.inklusport.search.client.ExternalRegistrationDto;
import com.inklusport.search.client.SportsServiceClient;
import com.inklusport.search.dto.EventParticipantResponse;
import com.inklusport.search.dto.PageResponse;
import com.inklusport.search.model.SearchUser;
import com.inklusport.search.repository.SearchUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * RF37 - Filtrar usuarios (inscritos) por evento especifico.
 * Las inscripciones son datos vivos de ink-ms-sports (cambian constantemente
 * con cada alta/baja), asi que se consultan en caliente via Feign y se
 * enriquecen con el perfil ya sincronizado en la coleccion local "users"
 * para poder filtrar por atributos como el tipo de discapacidad.
 */
@Service
@RequiredArgsConstructor
public class EventParticipantsService {

    private static final Map<String, Comparator<EventParticipantResponse>> SORTERS = Map.of(
            "name", Comparator.comparing(p -> nullSafe(p.getUserName())),
            "registrationDate", Comparator.comparing(EventParticipantResponse::getRegistrationDate,
                    Comparator.nullsLast(Comparator.naturalOrder())),
            "status", Comparator.comparing(p -> nullSafe(p.getRegistrationStatus()))
    );

    private final SportsServiceClient sportsServiceClient;
    private final SearchUserRepository searchUserRepository;
    private final SearchLogService searchLogService;

    public PageResponse<EventParticipantResponse> getParticipants(String eventId, String disability,
                                                                    String status, String sortBy, String direction,
                                                                    int page, int size, String requestedBy) {

        page = Math.max(page, 0);
        size = Math.max(size, 1);

        List<ExternalRegistrationDto> registrations;
        try {
            registrations = sportsServiceClient.getRegistrationsByEvent(eventId);
        } catch (Exception e) {
            registrations = List.of();
        }

        List<EventParticipantResponse> enriched = registrations.stream()
                .map(this::enrich)
                .filter(p -> disability == null || disability.isBlank()
                        || (p.getDisability() != null && p.getDisability().equalsIgnoreCase(disability)))
                .filter(p -> status == null || status.isBlank()
                        || p.getRegistrationStatus().equalsIgnoreCase(status))
                .sorted(resolveComparator(sortBy, direction))
                .toList();

        int fromIndex = Math.min(page * size, enriched.size());
        int toIndex = Math.min(fromIndex + size, enriched.size());
        List<EventParticipantResponse> pageContent = enriched.subList(fromIndex, toIndex);

        Map<String, Object> filters = new LinkedHashMap<>();
        filters.put("disability", disability);
        filters.put("status", status);
        searchLogService.record(requestedBy, "event:" + eventId, "EVENT_PARTICIPANTS", filters, enriched.size());

        return PageResponse.of(pageContent, enriched.size(), page, size);
    }

    private EventParticipantResponse enrich(ExternalRegistrationDto reg) {
        Optional<SearchUser> user = searchUserRepository.findById(reg.getUserId());

        return EventParticipantResponse.builder()
                .registrationId(reg.getId())
                .userId(reg.getUserId())
                .userName(user.map(SearchUser::getName).orElse("Usuario no sincronizado"))
                .userEmail(user.map(SearchUser::getEmail).orElse(null))
                .disability(user.map(SearchUser::getDisability).orElse(null))
                .eventId(reg.getEventId())
                .eventName(reg.getEventName())
                .registrationDate(reg.getRegistrationDate())
                .attended(reg.getAttended())
                .waitlistPosition(reg.getWaitlistPosition())
                .registrationStatus(deriveStatus(reg))
                .build();
    }

    private String deriveStatus(ExternalRegistrationDto reg) {
        if (Boolean.TRUE.equals(reg.getAttended())) {
            return "ATTENDED";
        }
        return reg.getWaitlistPosition() != null ? "WAITLIST" : "CONFIRMED";
    }

    private Comparator<EventParticipantResponse> resolveComparator(String sortBy, String direction) {
        Comparator<EventParticipantResponse> comparator = SORTERS.getOrDefault(sortBy, SORTERS.get("registrationDate"));
        return "desc".equalsIgnoreCase(direction) ? comparator.reversed() : comparator;
    }

    private static String nullSafe(String value) {
        return value == null ? "" : value;
    }
}
