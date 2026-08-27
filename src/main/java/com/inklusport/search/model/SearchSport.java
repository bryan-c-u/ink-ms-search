package com.inklusport.search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "sports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchSport {

    @Id
    private String id;

    @Field("source_id")
    private Integer sourceId;

    private String name;

    private String description;

    private String difficulty;

    @Field("required_materials")
    private String requiredMaterials;

    @Field("is_active")
    private Boolean isActive;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("synced_at")
    private LocalDateTime syncedAt;
}
