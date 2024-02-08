package com.pluser.medreg.domain.repo;

import com.pluser.medreg.domain.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IPatientRepo extends JpaRepository<Patient, UUID> {
}
