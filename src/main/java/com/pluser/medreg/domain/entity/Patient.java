package com.pluser.medreg.domain.entity;


import com.pluser.medreg.domain.entity.abs.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Patient extends Person {
    @OneToMany
    private Set<Talon> talons;
}
