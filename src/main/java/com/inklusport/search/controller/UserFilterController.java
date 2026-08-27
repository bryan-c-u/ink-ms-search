package com.inklusport.search.controller;

import com.inklusport.search.dto.PageResponse;
import com.inklusport.search.dto.UserFilterResponse;
import com.inklusport.search.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Filtros administrativos sobre usuarios (complementa RF37 y aporta a RF40
 * el ordenamiento dinamico de la tabla de usuarios).
 */
@RestController
@RequestMapping("/api/search/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserFilterController {

    private final UserSearchService userSearchService;

    @GetMapping
    public ResponseEntity<PageResponse<UserFilterResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) List<String> roles,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String disability,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(userSearchService.search(
                name, email, roles, isActive, disability, sortBy, direction, page, size));
    }
}
