package com.inklusport.search.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class SearchLogResponse {
    private String id;
    private String userId;
    private String query;
    private String searchType;
    private Map<String, Object> filters;
    private Integer resultsCount;
    private LocalDateTime createdAt;
}
