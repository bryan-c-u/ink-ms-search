package com.inklusport.search.service;

import com.inklusport.search.dto.*;
import com.inklusport.search.model.PopularSearch;
import com.inklusport.search.model.SearchDisability;
import com.inklusport.search.model.SearchEvent;
import com.inklusport.search.model.SearchSport;
import com.inklusport.search.model.SearchUser;
import com.inklusport.search.repository.PopularSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * RF38 - Buscador central: un mismo termino se busca a la vez en usuarios,
 * eventos, deportes y discapacidades usando los indices de texto declarados
 * en cada coleccion, y queda registrado para autocompletado (popular_searches).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GlobalSearchService {

    private static final int RESULTS_PER_TYPE = 10;

    private final MongoTemplate mongoTemplate;
    private final PopularSearchRepository popularSearchRepository;
    private final SearchLogService searchLogService;

    public GlobalSearchResponse search(String q, String requestedBy) {
        TextCriteria criteria = TextCriteria.forLanguage("spanish").matching(q);
        Query textQuery = TextQuery.queryText(criteria).sortByScore().limit(RESULTS_PER_TYPE);

        List<UserFilterResponse> users = mongoTemplate.find(textQuery, SearchUser.class).stream()
                .map(u -> UserFilterResponse.builder()
                        .id(u.getId()).name(u.getName()).email(u.getEmail())
                        .roles(u.getRoles()).isActive(u.getIsActive())
                        .disability(u.getDisability()).phone(u.getPhone())
                        .createdAt(u.getCreatedAt())
                        .build())
                .toList();

        List<EventFilterResponse> events = mongoTemplate.find(textQuery, SearchEvent.class).stream()
                .map(e -> EventFilterResponse.builder()
                        .id(e.getId()).name(e.getName()).description(e.getDescription())
                        .sportName(e.getSportName()).eventDate(e.getEventDate()).eventTime(e.getEventTime())
                        .location(e.getLocation()).maxCapacity(e.getMaxCapacity())
                        .availableCapacity(e.getAvailableCapacity()).status(e.getStatus())
                        .build())
                .toList();

        List<SportFilterResponse> sports = mongoTemplate.find(textQuery, SearchSport.class).stream()
                .map(s -> SportFilterResponse.builder()
                        .id(s.getSourceId()).name(s.getName()).description(s.getDescription())
                        .difficulty(s.getDifficulty()).isActive(s.getIsActive())
                        .build())
                .toList();

        List<DisabilityFilterResponse> disabilities = mongoTemplate.find(textQuery, SearchDisability.class).stream()
                .map(d -> DisabilityFilterResponse.builder()
                        .id(d.getSourceId()).name(d.getName()).description(d.getDescription())
                        .category(d.getCategory()).isActive(d.getIsActive())
                        .build())
                .toList();

        long total = users.size() + events.size() + sports.size() + disabilities.size();

        registerPopularSearch(q);
        Map<String, Object> filters = new LinkedHashMap<>();
        filters.put("scope", "global");
        searchLogService.record(requestedBy, q, "GLOBAL", filters, (int) total);

        return GlobalSearchResponse.builder()
                .query(q)
                .totalResults(total)
                .users(users)
                .events(events)
                .sports(sports)
                .disabilities(disabilities)
                .build();
    }

    public List<String> suggestions(String prefix, int limit) {
        return popularSearchRepository
                .findByQueryStartingWithIgnoreCaseOrderByScoreDesc(prefix, PageRequest.of(0, limit))
                .stream()
                .map(PopularSearch::getQuery)
                .toList();
    }

    public List<PopularSearchResponse> popular(int limit) {
        return popularSearchRepository.findAllByOrderByScoreDesc(PageRequest.of(0, limit)).stream()
                .map(p -> PopularSearchResponse.builder()
                        .query(p.getQuery())
                        .score(p.getScore())
                        .lastSearchedAt(p.getLastSearchedAt())
                        .build())
                .toList();
    }

    /**
     * Efecto secundario de analitica: si falla, no debe tumbar una busqueda
     * global que ya se resolvio correctamente.
     */
    private void registerPopularSearch(String query) {
        try {
            String normalized = query.trim().toLowerCase();
            PopularSearch entry = popularSearchRepository.findByQuery(normalized)
                    .orElseGet(() -> PopularSearch.builder()
                            .query(normalized)
                            .score(0L)
                            .hitCount(0L)
                            .build());

            entry.setScore(entry.getScore() + 1);
            entry.setHitCount(entry.getHitCount() + 1);
            entry.setLastSearchedAt(LocalDateTime.now());
            popularSearchRepository.save(entry);
        } catch (Exception e) {
            log.warn("No se pudo actualizar popular_searches para '{}': {}", query, e.getMessage());
        }
    }
}
