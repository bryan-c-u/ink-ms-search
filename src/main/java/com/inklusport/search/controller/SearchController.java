package com.inklusport.search.controller;

import com.inklusport.search.dto.GlobalSearchResponse;
import com.inklusport.search.dto.PopularSearchResponse;
import com.inklusport.search.dto.SearchLogResponse;
import com.inklusport.search.dto.SyncSummaryResponse;
import com.inklusport.search.service.DataSyncService;
import com.inklusport.search.service.GlobalSearchService;
import com.inklusport.search.service.SearchLogService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RF38 - Busqueda global del sistema: un unico campo que encuentra
 * usuarios, eventos, deportes y discapacidades. Incluye sugerencias
 * (autocompletado) y el disparador manual de sincronizacion del indice.
 */
@RestController
@RequestMapping("/api/search")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Validated
public class SearchController {

    private final GlobalSearchService globalSearchService;
    private final SearchLogService searchLogService;
    private final DataSyncService dataSyncService;

    @GetMapping("/global")
    public ResponseEntity<GlobalSearchResponse> globalSearch(
            @RequestParam @NotBlank String q,
            @AuthenticationPrincipal String requestedBy) {
        return ResponseEntity.ok(globalSearchService.search(q, requestedBy));
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> suggestions(
            @RequestParam @NotBlank String prefix,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(globalSearchService.suggestions(prefix, limit));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PopularSearchResponse>> popular(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(globalSearchService.popular(limit));
    }

    @GetMapping("/logs")
    public ResponseEntity<Page<SearchLogResponse>> logs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(searchLogService.getRecent(page, size));
    }

    /**
     * Fuerza una resincronizacion inmediata del indice (usuarios, deportes,
     * eventos y discapacidades) contra ink-ms-users e ink-ms-sports.
     */
    @PostMapping("/sync")
    public ResponseEntity<SyncSummaryResponse> sync() {
        return ResponseEntity.ok(dataSyncService.syncAll());
    }
}
