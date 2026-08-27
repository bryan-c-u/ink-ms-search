package com.inklusport.search.service;

import com.inklusport.search.dto.PageResponse;
import com.inklusport.search.dto.UserFilterResponse;
import com.inklusport.search.model.SearchUser;
import com.inklusport.search.util.RegexUtils;
import com.inklusport.search.util.SortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Filtrado y ordenamiento dinamico de usuarios (RF40), usado tambien como
 * base de enriquecimiento para RF37 (participantes de un evento).
 */
@Service
@RequiredArgsConstructor
public class UserSearchService {

    private static final Map<String, String> SORT_FIELDS = Map.of(
            "name", "name",
            "email", "email",
            "isActive", "is_active",
            "createdAt", "created_at"
    );

    private final MongoTemplate mongoTemplate;

    public PageResponse<UserFilterResponse> search(String name, String email, List<String> roles,
                                                     Boolean isActive, String disability,
                                                     String sortBy, String direction,
                                                     int page, int size) {

        Criteria criteria = new Criteria();
        List<Criteria> conditions = new java.util.ArrayList<>();

        if (name != null && !name.isBlank()) {
            conditions.add(Criteria.where("name").regex(RegexUtils.literalContains(name), "i"));
        }
        if (email != null && !email.isBlank()) {
            conditions.add(Criteria.where("email").regex(RegexUtils.literalContains(email), "i"));
        }
        if (roles != null && !roles.isEmpty()) {
            conditions.add(Criteria.where("roles").in(roles));
        }
        if (isActive != null) {
            conditions.add(Criteria.where("is_active").is(isActive));
        }
        if (disability != null && !disability.isBlank()) {
            conditions.add(Criteria.where("disability").regex(RegexUtils.literalContains(disability), "i"));
        }

        if (!conditions.isEmpty()) {
            criteria = criteria.andOperator(conditions.toArray(new Criteria[0]));
        }

        Query query = new Query(criteria);
        long total = mongoTemplate.count(query, SearchUser.class);

        query.with(SortUtils.resolve(sortBy, direction, SORT_FIELDS, "name"));
        query.with(PageRequest.of(page, size));

        List<UserFilterResponse> content = mongoTemplate.find(query, SearchUser.class).stream()
                .map(this::toResponse)
                .toList();

        return PageResponse.of(content, total, page, size);
    }

    private UserFilterResponse toResponse(SearchUser user) {
        return UserFilterResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isActive(user.getIsActive())
                .disability(user.getDisability())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
