package com.inklusport.search.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class EventFilterResponse {
    private String id;
    private String name;
    private String description;
    private String sportName;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private String location;
    private Integer maxCapacity;
    private Integer availableCapacity;
    private String status;
}
