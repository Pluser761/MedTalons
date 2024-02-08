package com.pluser.medreg.domain.repo;

import com.pluser.medreg.domain.entity.Doctor;
import com.pluser.medreg.domain.entity.Patient;
import com.pluser.medreg.domain.entity.Specialization;
import com.pluser.medreg.domain.entity.Talon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITalonRepo extends JpaRepository<Talon, UUID> {
    void deleteTalonsByStartDateTimeGreaterThanEqualAndStopDateTimeLessThanEqual(LocalDateTime start, LocalDateTime stop);
    List<Talon> findAllByPatientUuid(UUID uuid);

    @Query(value = "select t from Talon t " +
            "where (" +
            "(t.specialization = :specialization) and " +
            "(t.startDateTime >= :startDateTime and t.stopDateTime <= :stopDateTime) " +
            ")"
    )
    List<Talon> findAvailableTickets(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("stopDateTime") LocalDateTime stopDateTime,
            @Param("specialization") Specialization specialization);

    @Query(value = "select t from Talon t " +
            "where (" +
            "(t.doctor = :doctor) and " +
            "(t.startDateTime >= :startDateTime and t.stopDateTime <= :stopDateTime) " +
            ")"
    )
    List<Talon> findAvailableTickets(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("stopDateTime") LocalDateTime stopDateTime,
            @Param("doctor") Doctor doctor);

    @Query(value = "select t from Talon t " +
            "where (" +
            "(t.doctor = :doctor) and " +
            "(t.specialization = :specialization) and " +
            "(t.startDateTime >= :startDateTime and t.stopDateTime <= :stopDateTime) " +
            ")"
    )
    List<Talon> findAvailableTickets(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("stopDateTime") LocalDateTime stopDateTime,
            @Param("doctor") Doctor doctor,
            @Param("specialization") Specialization specialization);

    @Query(value = "select t from Talon t " +
            "where (" +
            "(t.specialization = :specialization) and " +
            "(t.startDateTime >= :startDateTime and t.stopDateTime <= :stopDateTime) " +
            ")"
    )
    List<Talon> findAvailableTickets(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("stopDateTime") LocalDateTime stopDateTime);
}
