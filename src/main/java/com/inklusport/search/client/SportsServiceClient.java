package com.inklusport.search.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ink-ms-sports", url = "${sports.service.url:http://localhost:3003}", fallback = SportsServiceFallback.class)
public interface SportsServiceClient {

    @GetMapping("/api/sports")
    List<ExternalSportDto> getAllSports();

    @GetMapping("/api/disabilities")
    List<ExternalDisabilityDto> getAllDisabilities();

    @GetMapping("/api/events")
    List<ExternalEventDto> getAllEvents();

    @GetMapping("/api/internal/registrations/event/{eventId}")
    List<ExternalRegistrationDto> getRegistrationsByEvent(@PathVariable("eventId") String eventId);
}
