package com.inklusport.search.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Envoltorio de paginacion, con la misma forma que usa ink-ms-admin
 * (content / totalElements / totalPages / currentPage).
 */
@Data
@Builder
public class PageResponse<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;

    public static <T> PageResponse<T> of(List<T> content, long totalElements, int page, int size) {
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
        return PageResponse.<T>builder()
                .content(content)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .currentPage(page)
                .build();
    }
}
