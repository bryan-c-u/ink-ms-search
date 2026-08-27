package com.inklusport.search.client;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExternalSportDto {
    private Integer id;
    private String name;
    private String description;
    private String difficulty;
    private String requiredMaterials;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
