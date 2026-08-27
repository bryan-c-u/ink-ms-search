package com.inklusport.search.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Resultado de RF38 (buscador central): agrupa coincidencias de las
 * distintas entidades para un mismo termino de busqueda.
 */
@Data
@Builder
public class GlobalSearchResponse {
    private String query;
    private long totalResults;
    private List<UserFilterResponse> users;
    private List<EventFilterResponse> events;
    private List<SportFilterResponse> sports;
    private List<DisabilityFilterResponse> disabilities;
}
