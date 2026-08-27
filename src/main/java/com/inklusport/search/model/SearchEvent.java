package com.inklusport.search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Copia de solo lectura de los eventos de ink-ms-sports (RF39: filtrado por
 * fecha/ubicacion/categoria).
 */
@Document(collection = "events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchEvent {

    @Id
    private String id;

    @Field("sport_id")
    private Integer sportId;

    @Field("sport_name")
    private String sportName;

    private String name;

    private String description;

    @Field("event_date")
    private LocalDate eventDate;

    @Field("event_time")
    private LocalTime eventTime;

    private String location;

    @Field("max_capacity")
    private Integer maxCapacity;

    @Field("available_capacity")
    private Integer availableCapacity;

    private String status;

    @Field("created_by")
    private String createdBy;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("synced_at")
    private LocalDateTime syncedAt;
}
