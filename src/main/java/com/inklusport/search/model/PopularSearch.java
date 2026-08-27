package com.inklusport.search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "popular_searches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularSearch {

    @Id
    private String id;

    private String query;

    private Long score;

    @Field("hit_count")
    private Long hitCount;

    @Field("last_searched_at")
    private LocalDateTime lastSearchedAt;
}
