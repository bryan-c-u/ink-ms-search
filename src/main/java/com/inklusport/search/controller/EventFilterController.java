package com.inklusport.search.controller;

import com.inklusport.search.dto.EventFilterResponse;
import com.inklusport.search.dto.EventParticipantResponse;
import com.inklusport.search.dto.PageResponse;
import com.inklusport.search.service.EventParticipantsService;
import com.inklusport.search.service.EventSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * RF37 - Filtrar usuarios (inscritos) por evento y
 * RF39 - Filtrado de eventos por fecha/ubicacion/categoria.
 */
@RestController
@RequestMapping("/api/search/events")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class EventFilterController {

    private final EventSearchService eventSearchService;
    private final EventParticipantsService eventParticipantsService;

    @GetMapping
    public ResponseEntity<PageResponse<EventFilterResponse>> search(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) String sportName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(eventSearchService.search(
                location, dateFrom, dateTo, sportName, status, sortBy, direction, page, size));
    }

    @GetMapping("/{eventId}/participants")
    public ResponseEntity<PageResponse<EventParticipantResponse>> getParticipants(
            @PathVariable String eventId,
            @RequestParam(required = false) String disability,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal String requestedBy) {

        return ResponseEntity.ok(eventParticipantsService.getParticipants(
                eventId, disability, status, sortBy, direction, page, size, requestedBy));
    }
}
