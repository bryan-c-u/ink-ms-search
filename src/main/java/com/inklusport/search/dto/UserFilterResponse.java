package com.inklusport.search.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserFilterResponse {
    private String id;
    private String name;
    private String email;
    private List<String> roles;
    private Boolean isActive;
    private String disability;
    private String phone;
    private LocalDateTime createdAt;
}
