package com.pluser.medreg.domain.entity.abs;


import jakarta.annotation.Nullable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(of = {"uuid"})
public class Person {
    @Id
    @GeneratedValue
    private UUID uuid;

    // @Nullable для облегчения демонстрации
    @Nullable
    private String lastName;
    private String firstName;
    @Nullable
    private String patronymic;
}
