package com.inklusport.search.service;

import com.inklusport.search.dto.EventFilterResponse;
import com.inklusport.search.dto.PageResponse;
import com.inklusport.search.model.SearchEvent;
import com.inklusport.search.util.RegexUtils;
import com.inklusport.search.util.SortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RF39 - Filtrado de eventos por fecha/ubicacion/categoria (deporte) y
 * RF40 - Ordenamiento dinamico.
 */
@Service
@RequiredArgsConstructor
public class EventSearchService {

    private static final Map<String, String> SORT_FIELDS = Map.of(
            "name", "name",
            "eventDate", "event_date",
            "location", "location",
            "sportName", "sport_name",
            "status", "status"
    );

    private final MongoTemplate mongoTemplate;

    public PageResponse<EventFilterResponse> search(String location, LocalDate dateFrom, LocalDate dateTo,
                                                      String sportName, String status,
                                                      String sortBy, String direction,
                                                      int page, int size) {

        List<Criteria> conditions = new ArrayList<>();

        if (location != null && !location.isBlank()) {
            conditions.add(Criteria.where("location").regex(RegexUtils.literalContains(location), "i"));
        }
        if (dateFrom != null || dateTo != null) {
            Criteria dateCriteria = Criteria.where("event_date");
            if (dateFrom != null) {
                dateCriteria = dateCriteria.gte(dateFrom);
            }
            if (dateTo != null) {
                dateCriteria = dateCriteria.lte(dateTo);
            }
            conditions.add(dateCriteria);
        }
        if (sportName != null && !sportName.isBlank()) {
            conditions.add(Criteria.where("sport_name").regex(RegexUtils.literalContains(sportName), "i"));
        }
        if (status != null && !status.isBlank()) {
            conditions.add(Criteria.where("status").is(status));
        }

        Criteria criteria = conditions.isEmpty()
                ? new Criteria()
                : new Criteria().andOperator(conditions.toArray(new Criteria[0]));

        Query query = new Query(criteria);
        long total = mongoTemplate.count(query, SearchEvent.class);

        query.with(SortUtils.resolve(sortBy, direction, SORT_FIELDS, "eventDate"));
        query.with(PageRequest.of(page, size));

        List<EventFilterResponse> content = mongoTemplate.find(query, SearchEvent.class).stream()
                .map(this::toResponse)
                .toList();

        return PageResponse.of(content, total, page, size);
    }

    private EventFilterResponse toResponse(SearchEvent event) {
        return EventFilterResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .sportName(event.getSportName())
                .eventDate(event.getEventDate())
                .eventTime(event.getEventTime())
                .location(event.getLocation())
                .maxCapacity(event.getMaxCapacity())
                .availableCapacity(event.getAvailableCapacity())
                .status(event.getStatus())
                .build();
    }
}
