package com.inklusport.search.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SyncSummaryResponse {
    private int usersSynced;
    private int sportsSynced;
    private int eventsSynced;
    private int disabilitiesSynced;
    private LocalDateTime syncedAt;
}
