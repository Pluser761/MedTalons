package com.pluser.medreg.domain.repo;

import com.pluser.medreg.domain.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ISpecializationRepo extends JpaRepository<Specialization, UUID> {
    Optional<Specialization> findByName(String name);
    Set<Specialization> findAllByNameIn(List<String> name);
}
