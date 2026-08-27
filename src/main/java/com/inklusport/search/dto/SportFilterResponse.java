package com.inklusport.search.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SportFilterResponse {
    private Integer id;
    private String name;
    private String description;
    private String difficulty;
    private Boolean isActive;
}
