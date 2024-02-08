package com.pluser.medreg.controller.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TalonResponse {
    private UUID id;

    private UUID doctorId;

    private UUID patientId;

    private UUID specializationId;

    private LocalDateTime startDateTime;

    private LocalDateTime stopDateTime;
    public TalonResponse() {

    }
}
