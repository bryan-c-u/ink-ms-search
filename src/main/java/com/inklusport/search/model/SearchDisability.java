package com.inklusport.search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "disabilities")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDisability {

    @Id
    private String id;

    @Field("source_id")
    private Integer sourceId;

    private String name;

    private String description;

    private String category;

    @Field("is_active")
    private Boolean isActive;
}
