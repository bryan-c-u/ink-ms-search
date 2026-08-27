package com.inklusport.search.service;

import com.inklusport.search.client.*;
import com.inklusport.search.dto.SyncSummaryResponse;
import com.inklusport.search.model.SearchDisability;
import com.inklusport.search.model.SearchEvent;
import com.inklusport.search.model.SearchSport;
import com.inklusport.search.model.SearchUser;
import com.inklusport.search.repository.SearchDisabilityRepository;
import com.inklusport.search.repository.SearchEventRepository;
import com.inklusport.search.repository.SearchSportRepository;
import com.inklusport.search.repository.SearchUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Sincroniza el indice de busqueda (MongoDB) con los datos "fuente de verdad"
 * que viven en ink-ms-users e ink-ms-sports. Se ejecuta periodicamente
 * (DataSyncScheduler) y bajo demanda (POST /api/search/sync).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DataSyncService {

    private final UsersServiceClient usersServiceClient;
    private final SportsServiceClient sportsServiceClient;

    private final SearchUserRepository searchUserRepository;
    private final SearchSportRepository searchSportRepository;
    private final SearchEventRepository searchEventRepository;
    private final SearchDisabilityRepository searchDisabilityRepository;

    /**
     * Cada sub-sincronizacion se aisla: si ink-ms-users esta caido no debe
     * impedir que se actualicen los deportes/eventos/discapacidades de
     * ink-ms-sports (y viceversa). En caso de fallo se conserva el ultimo
     * estado sincronizado de esa coleccion.
     */
    public SyncSummaryResponse syncAll() {
        int users = safeSync("usuarios", this::syncUsers);
        int sports = safeSync("deportes", this::syncSports);
        int events = safeSync("eventos", this::syncEvents);
        int disabilities = safeSync("discapacidades", this::syncDisabilities);

        log.info("Sincronizacion completa - usuarios: {}, deportes: {}, eventos: {}, discapacidades: {}",
                users, sports, events, disabilities);

        return SyncSummaryResponse.builder()
                .usersSynced(users)
                .sportsSynced(sports)
                .eventsSynced(events)
                .disabilitiesSynced(disabilities)
                .syncedAt(LocalDateTime.now())
                .build();
    }

    private int safeSync(String label, java.util.function.IntSupplier syncFn) {
        try {
            return syncFn.getAsInt();
        } catch (Exception e) {
            log.warn("No se pudo sincronizar '{}': {}", label, e.getMessage());
            return 0;
        }
    }

    public int syncUsers() {
        List<ExternalUserDto> users = usersServiceClient.getAllUsers();
        LocalDateTime now = LocalDateTime.now();

        List<SearchUser> docs = users.stream()
                .map(u -> SearchUser.builder()
                        .id(u.getId())
                        .name(u.getFullName())
                        .email(u.getEmail())
                        .roles(u.getRoles())
                        .isActive(u.getIsActive())
                        .disability(u.getDisability())
                        .phone(u.getPhone())
                        .createdAt(u.getCreatedAt())
                        .updatedAt(u.getUpdatedAt())
                        .syncedAt(now)
                        .build())
                .toList();

        if (!docs.isEmpty()) {
            searchUserRepository.saveAll(docs);
        }
        return docs.size();
    }

    public int syncSports() {
        List<ExternalSportDto> sports = sportsServiceClient.getAllSports();
        LocalDateTime now = LocalDateTime.now();

        List<SearchSport> docs = sports.stream()
                .map(s -> SearchSport.builder()
                        .id(String.valueOf(s.getId()))
                        .sourceId(s.getId())
                        .name(s.getName())
                        .description(s.getDescription())
                        .difficulty(s.getDifficulty())
                        .requiredMaterials(s.getRequiredMaterials())
                        .isActive(s.getIsActive())
                        .createdAt(s.getCreatedAt())
                        .syncedAt(now)
                        .build())
                .toList();

        if (!docs.isEmpty()) {
            searchSportRepository.saveAll(docs);
        }
        return docs.size();
    }

    public int syncEvents() {
        List<ExternalEventDto> events = sportsServiceClient.getAllEvents();
        LocalDateTime now = LocalDateTime.now();

        List<SearchEvent> docs = events.stream()
                .map(e -> SearchEvent.builder()
                        .id(e.getId())
                        .sportId(e.getSportId())
                        .sportName(e.getSportName())
                        .name(e.getName())
                        .description(e.getDescription())
                        .eventDate(e.getEventDate())
                        .eventTime(e.getEventTime())
                        .location(e.getLocation())
                        .maxCapacity(e.getMaxCapacity())
                        .availableCapacity(e.getAvailableCapacity())
                        .status(e.getStatus())
                        .createdBy(e.getCreatedBy())
                        .createdAt(e.getCreatedAt())
                        .syncedAt(now)
                        .build())
                .toList();

        if (!docs.isEmpty()) {
            searchEventRepository.saveAll(docs);
        }
        return docs.size();
    }

    public int syncDisabilities() {
        List<ExternalDisabilityDto> disabilities = sportsServiceClient.getAllDisabilities();

        List<SearchDisability> docs = disabilities.stream()
                .map(d -> SearchDisability.builder()
                        .id(String.valueOf(d.getId()))
                        .sourceId(d.getId())
                        .name(d.getName())
                        .description(d.getDescription())
                        .category(d.getCategory())
                        .isActive(d.getIsActive())
                        .build())
                .toList();

        if (!docs.isEmpty()) {
            searchDisabilityRepository.saveAll(docs);
        }
        return docs.size();
    }
}
