package com.inklusport.search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Registro de auditoria/analitica de busquedas administrativas (RF38).
 */
@Document(collection = "search_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchLog {

    @Id
    private String id;

    @Field("user_id")
    private String userId;

    private String query;

    @Field("search_type")
    private String searchType;

    private Map<String, Object> filters;

    @Field("results_count")
    private Integer resultsCount;

    @Field("created_at")
    private LocalDateTime createdAt;
}
