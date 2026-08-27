package com.inklusport.search.client;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Representa la respuesta de GET /api/internal/users en ink-ms-users.
 * Se define localmente porque ink-ms-common no esta disponible en este repo
 * independiente (mismo criterio que el resto de los microservicios).
 */
@Data
public class ExternalUserDto {
    private String id;
    private String email;
    private String fullName;
    private String phone;
    private String disability;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> roles;
}
