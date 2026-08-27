package com.inklusport.search.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisabilityFilterResponse {
    private Integer id;
    private String name;
    private String description;
    private String category;
    private Boolean isActive;
}
