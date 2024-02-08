package com.pluser.medreg.domain.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Talon {
    @Id
    @GeneratedValue
    private UUID uuid;

    @ManyToOne
    @Nullable
    private Doctor doctor;

    @ManyToOne
    @Nullable
    private Patient patient;

    @ManyToOne
    private Specialization specialization;

    private LocalDateTime startDateTime;

    private LocalDateTime stopDateTime;
}
