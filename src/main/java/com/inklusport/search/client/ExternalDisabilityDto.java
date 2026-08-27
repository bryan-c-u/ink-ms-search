package com.inklusport.search.client;

import lombok.Data;

@Data
public class ExternalDisabilityDto {
    private Integer id;
    private String name;
    private String description;
    private String category;
    private Boolean isActive;
}
