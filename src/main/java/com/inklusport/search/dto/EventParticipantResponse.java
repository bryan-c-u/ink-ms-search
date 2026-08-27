package com.inklusport.search.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Resultado de RF37: inscrito de un evento, enriquecido con los datos de
 * perfil sincronizados desde ink-ms-users para poder filtrar por
 * discapacidad, nombre, etc.
 */
@Data
@Builder
public class EventParticipantResponse {
    private String registrationId;
    private String userId;
    private String userName;
    private String userEmail;
    private String disability;
    private String eventId;
    private String eventName;
    private LocalDateTime registrationDate;
    private Boolean attended;
    private Integer waitlistPosition;

    /** CONFIRMED, WAITLIST o ATTENDED, derivado de attended/waitlistPosition. */
    private String registrationStatus;
}
