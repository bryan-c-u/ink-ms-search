package com.inklusport.search.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SportsServiceFallback implements SportsServiceClient {

    @Override
    public List<ExternalSportDto> getAllSports() {
        log.warn("Sports MS no disponible. No se pudo sincronizar el indice de deportes.");
        return List.of();
    }

    @Override
    public List<ExternalDisabilityDto> getAllDisabilities() {
        log.warn("Sports MS no disponible. No se pudo sincronizar el indice de discapacidades.");
        return List.of();
    }

    @Override
    public List<ExternalEventDto> getAllEvents() {
        log.warn("Sports MS no disponible. No se pudo sincronizar el indice de eventos.");
        return List.of();
    }

    @Override
    public List<ExternalRegistrationDto> getRegistrationsByEvent(String eventId) {
        log.warn("Sports MS no disponible. No se pudieron obtener los inscritos del evento: {}", eventId);
        return List.of();
    }
}
