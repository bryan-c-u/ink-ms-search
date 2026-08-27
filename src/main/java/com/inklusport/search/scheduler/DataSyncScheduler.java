package com.inklusport.search.scheduler;

import com.inklusport.search.service.DataSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSyncScheduler {

    private final DataSyncService dataSyncService;

    /**
     * Primera sincronizacion al levantar el microservicio, para que el
     * indice no arranque vacio.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void syncOnStartup() {
        log.info("[DataSyncScheduler] Sincronizacion inicial del indice de busqueda");
        try {
            dataSyncService.syncAll();
        } catch (Exception e) {
            log.error("[DataSyncScheduler] Fallo la sincronizacion inicial: {}", e.getMessage());
        }
    }

    /**
     * Resincronizacion periodica (por defecto cada 5 minutos).
     */
    @Scheduled(fixedRateString = "${search.sync.interval-ms:300000}")
    public void syncPeriodically() {
        log.info("[DataSyncScheduler] Sincronizacion periodica del indice de busqueda");
        try {
            dataSyncService.syncAll();
        } catch (Exception e) {
            log.error("[DataSyncScheduler] Fallo la sincronizacion periodica: {}", e.getMessage());
        }
    }
}
