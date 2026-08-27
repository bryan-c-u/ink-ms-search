package com.inklusport.search.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UsersServiceFallback implements UsersServiceClient {

    @Override
    public List<ExternalUserDto> getAllUsers() {
        log.warn("Users MS no disponible. No se pudo sincronizar el indice de usuarios.");
        return List.of();
    }
}
