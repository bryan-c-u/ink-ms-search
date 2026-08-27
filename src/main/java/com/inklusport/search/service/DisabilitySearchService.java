package com.inklusport.search.service;

import com.inklusport.search.dto.DisabilityFilterResponse;
import com.inklusport.search.dto.PageResponse;
import com.inklusport.search.model.SearchDisability;
import com.inklusport.search.util.RegexUtils;
import com.inklusport.search.util.SortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DisabilitySearchService {

    private static final Map<String, String> SORT_FIELDS = Map.of(
            "name", "name",
            "category", "category",
            "isActive", "is_active"
    );

    private final MongoTemplate mongoTemplate;

    public PageResponse<DisabilityFilterResponse> search(String name, String category, Boolean isActive,
                                                           String sortBy, String direction,
                                                           int page, int size) {

        List<Criteria> conditions = new ArrayList<>();
        if (name != null && !name.isBlank()) {
            conditions.add(Criteria.where("name").regex(RegexUtils.literalContains(name), "i"));
        }
        if (category != null && !category.isBlank()) {
            conditions.add(Criteria.where("category").is(category));
        }
        if (isActive != null) {
            conditions.add(Criteria.where("is_active").is(isActive));
        }

        Criteria criteria = conditions.isEmpty()
                ? new Criteria()
                : new Criteria().andOperator(conditions.toArray(new Criteria[0]));

        Query query = new Query(criteria);
        long total = mongoTemplate.count(query, SearchDisability.class);

        query.with(SortUtils.resolve(sortBy, direction, SORT_FIELDS, "name"));
        query.with(PageRequest.of(page, size));

        List<DisabilityFilterResponse> content = mongoTemplate.find(query, SearchDisability.class).stream()
                .map(d -> DisabilityFilterResponse.builder()
                        .id(d.getSourceId())
                        .name(d.getName())
                        .description(d.getDescription())
                        .category(d.getCategory())
                        .isActive(d.getIsActive())
                        .build())
                .toList();

        return PageResponse.of(content, total, page, size);
    }
}
