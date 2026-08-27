package com.inklusport.search.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.stereotype.Component;

/**
 * Crea explicitamente los indices de search_db (texto ponderado + filtros)
 * replicando el script de referencia del microservicio, para no depender
 * unicamente de "auto-index-creation" y para fijar nombre/idioma/pesos.
 * createIndex es idempotente: si el indice ya existe con la misma
 * definicion, Mongo no hace nada.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MongoIndexInitializer implements ApplicationRunner {

    private final MongoTemplate mongoTemplate;

    @Override
    public void run(ApplicationArguments args) {
        try {
            createUsersIndexes();
            createSportsIndexes();
            createEventsIndexes();
            createDisabilitiesIndexes();
            createSearchLogIndexes();
            createPopularSearchesIndexes();
            log.info("[MongoIndexInitializer] Indices de search_db verificados/creados");
        } catch (Exception e) {
            log.warn("[MongoIndexInitializer] No se pudieron asegurar los indices: {}", e.getMessage());
        }
    }

    private void createUsersIndexes() {
        IndexOperations ops = mongoTemplate.indexOps("users");
        ops.ensureIndex(TextIndexDefinition.builder()
                .named("idx_users_search")
                .withDefaultLanguage("spanish")
                .onField("name", 3F)
                .onField("email", 2F)
                .onField("roles", 1F)
                .build());
        ops.ensureIndex(new Index().on("is_active", Sort.Direction.ASC));
        ops.ensureIndex(new Index().on("roles", Sort.Direction.ASC));
    }

    private void createSportsIndexes() {
        IndexOperations ops = mongoTemplate.indexOps("sports");
        ops.ensureIndex(TextIndexDefinition.builder()
                .named("idx_sports_search")
                .withDefaultLanguage("spanish")
                .onField("name", 3F)
                .onField("description", 1F)
                .build());
        ops.ensureIndex(new Index().on("difficulty", Sort.Direction.ASC));
        ops.ensureIndex(new Index().on("is_active", Sort.Direction.ASC));
    }

    private void createEventsIndexes() {
        IndexOperations ops = mongoTemplate.indexOps("events");
        ops.ensureIndex(TextIndexDefinition.builder()
                .named("idx_events_search")
                .withDefaultLanguage("spanish")
                .onField("name", 3F)
                .onField("location", 2F)
                .onField("description", 1F)
                .build());
        ops.ensureIndex(new Index().on("event_date", Sort.Direction.ASC));
        ops.ensureIndex(new Index().on("location", Sort.Direction.ASC));
        ops.ensureIndex(new Index().on("status", Sort.Direction.ASC));
        ops.ensureIndex(new Index()
                .on("event_date", Sort.Direction.ASC)
                .on("location", Sort.Direction.ASC));
        ops.ensureIndex(new Index().on("sport_name", Sort.Direction.ASC));
    }

    private void createDisabilitiesIndexes() {
        IndexOperations ops = mongoTemplate.indexOps("disabilities");
        ops.ensureIndex(TextIndexDefinition.builder()
                .named("idx_disabilities_search")
                .withDefaultLanguage("spanish")
                .onField("name", 3F)
                .onField("description", 1F)
                .build());
        ops.ensureIndex(new Index().on("category", Sort.Direction.ASC));
        ops.ensureIndex(new Index().on("is_active", Sort.Direction.ASC));
    }

    private void createSearchLogIndexes() {
        IndexOperations ops = mongoTemplate.indexOps("search_log");
        ops.ensureIndex(new Index().on("created_at", Sort.Direction.DESC));
        ops.ensureIndex(new Index().on("user_id", Sort.Direction.ASC));
        ops.ensureIndex(TextIndexDefinition.builder()
                .named("idx_search_log_query")
                .onField("query")
                .build());
    }

    private void createPopularSearchesIndexes() {
        IndexOperations ops = mongoTemplate.indexOps("popular_searches");
        ops.ensureIndex(new Index().on("query", Sort.Direction.ASC).unique());
        ops.ensureIndex(new Index().on("score", Sort.Direction.DESC));
    }
}
