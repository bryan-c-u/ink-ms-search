package com.inklusport.search.service;

import com.inklusport.search.dto.SearchLogResponse;
import com.inklusport.search.model.SearchLog;
import com.inklusport.search.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Auditoria de busquedas administrativas: cada filtro/busqueda relevante
 * queda registrado en la coleccion search_log, para trazabilidad y para
 * alimentar el ranking de popular_searches.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SearchLogService {

    private final SearchLogRepository searchLogRepository;

    /**
     * El registro de auditoria es un efecto secundario: si Mongo falla al
     * escribir el log, eso NUNCA debe tumbar una busqueda que ya se
     * resolvio correctamente, asi que el error se registra y se descarta.
     */
    public void record(String userId, String query, String searchType, Map<String, Object> filters, int resultsCount) {
        try {
            SearchLog logEntry = SearchLog.builder()
                    .userId(userId)
                    .query(query)
                    .searchType(searchType)
                    .filters(filters)
                    .resultsCount(resultsCount)
                    .createdAt(LocalDateTime.now())
                    .build();
            searchLogRepository.save(logEntry);
        } catch (Exception e) {
            log.warn("No se pudo registrar el log de busqueda (tipo={}, query={}): {}", searchType, query, e.getMessage());
        }
    }

    public Page<SearchLogResponse> getRecent(int page, int size) {
        return searchLogRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size))
                .map(entry -> SearchLogResponse.builder()
                        .id(entry.getId())
                        .userId(entry.getUserId())
                        .query(entry.getQuery())
                        .searchType(entry.getSearchType())
                        .filters(entry.getFilters())
                        .resultsCount(entry.getResultsCount())
                        .createdAt(entry.getCreatedAt())
                        .build());
    }
}
