package com.inklusport.search.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PopularSearchResponse {
    private String query;
    private Long score;
    private LocalDateTime lastSearchedAt;
}
