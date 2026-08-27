package com.inklusport.search.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ink-ms-users", url = "${users.service.url:http://localhost:3003}", fallback = UsersServiceFallback.class)
public interface UsersServiceClient {

    @GetMapping("/api/internal/users")
    List<ExternalUserDto> getAllUsers();
}
