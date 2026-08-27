package com.inklusport.search.util;

import org.springframework.data.domain.Sort;

import java.util.Map;

/**
 * RF40 - Ordenamiento dinamico: resuelve el par (sortBy, direction) que llega
 * por query param contra una lista blanca de campos por coleccion, para
 * evitar ordenar (o inyectar) por campos arbitrarios de Mongo.
 */
public final class SortUtils {

    private SortUtils() {
    }

    public static Sort resolve(String sortBy, String direction, Map<String, String> allowedFields, String defaultField) {
        String field = (sortBy != null && allowedFields.containsKey(sortBy))
                ? allowedFields.get(sortBy)
                : allowedFields.getOrDefault(defaultField, defaultField);

        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, field);
    }
}
