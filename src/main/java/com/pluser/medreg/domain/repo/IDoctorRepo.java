package com.pluser.medreg.domain.repo;

import com.pluser.medreg.domain.entity.Doctor;
import com.pluser.medreg.domain.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDoctorRepo extends JpaRepository<Doctor, UUID> {
    Optional<Doctor> findDoctorByFirstName(String name);
    List<Doctor> findDoctorsBySpecializationsContaining(Specialization specialization);

    // кастомный метод нахождения свободных докторов в промежутке времени для талона
    @Query(value = "select d from Doctor d left join d.talons t " +
            "where ( (:specialization member of d.specializations) and (" +
                "(t.startDateTime <= :startDateTime and t.stopDateTime >= :stopDateTime) or " +
                "(t.startDateTime >= :startDateTime and t.startDateTime < :stopDateTime) or " +
                "(t.stopDateTime > :startDateTime and t.stopDateTime < :stopDateTime) " +
            ") )"
    )
    List<Doctor> findDoctorsWithInterceptedTickets(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("stopDateTime") LocalDateTime stopDateTime,
            @Param("specialization") Specialization specialization);

}
