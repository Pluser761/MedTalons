package com.pluser.medreg.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Specialization {
    @Id
    @GeneratedValue
    private UUID uuid;

    private String name;

    @DurationUnit(ChronoUnit.MINUTES)
    private Duration duration = Duration.of(15, ChronoUnit.MINUTES);

    @ManyToMany(mappedBy = "specializations")
    private Set<Doctor> doctors;

    @OneToMany(mappedBy = "specialization")
    private Set<Talon> talons;
}
