package com.inklusport.search.controller;

import com.inklusport.search.dto.DisabilityFilterResponse;
import com.inklusport.search.dto.PageResponse;
import com.inklusport.search.service.DisabilitySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search/disabilities")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class DisabilityFilterController {

    private final DisabilitySearchService disabilitySearchService;

    @GetMapping
    public ResponseEntity<PageResponse<DisabilityFilterResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(disabilitySearchService.search(name, category, isActive, sortBy, direction, page, size));
    }
}
