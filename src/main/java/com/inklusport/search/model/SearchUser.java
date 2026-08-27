package com.inklusport.search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Copia de solo lectura de los usuarios de ink-ms-users, indexada para
 * busqueda de texto y filtros (RF37, RF38). Sincronizada por DataSyncService.
 */
@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchUser {

    @Id
    private String id;

    private String name;

    private String email;

    private List<String> roles;

    @Field("is_active")
    private Boolean isActive;

    private String disability;

    private String phone;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("synced_at")
    private LocalDateTime syncedAt;
}
