package com.inklusport.search.client;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExternalRegistrationDto {
    private String id;
    private String userId;
    private String eventId;
    private String eventName;
    private LocalDateTime registrationDate;
    private Boolean attended;
    private Integer waitlistPosition;
}
