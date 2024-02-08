package com.pluser.medreg.controller;

import com.pluser.medreg.controller.payload.TalonRequest;
import com.pluser.medreg.controller.payload.TalonResponse;
import com.pluser.medreg.domain.entity.Doctor;
import com.pluser.medreg.domain.entity.Specialization;
import com.pluser.medreg.domain.entity.Talon;
import com.pluser.medreg.domain.repo.IDoctorRepo;
import com.pluser.medreg.domain.repo.IPatientRepo;
import com.pluser.medreg.domain.repo.ISpecializationRepo;
import com.pluser.medreg.domain.repo.ITalonRepo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("talon")
@AllArgsConstructor
public class TalonController {
    private final ITalonRepo talonRepo;
    private final ISpecializationRepo specializationRepo;
    private final IDoctorRepo doctorRepo;
    private final IPatientRepo patientRepo;

    @PostMapping("available")
    public List<TalonResponse> getAvailableTalons(@RequestBody TalonRequest request) {
        Specialization spec = null;
        if (request.specializationID != null)
            spec = specializationRepo.getReferenceById(request.specializationID);

        Doctor doc = null;
        if (request.doctorID != null)
            doc = doctorRepo.getReferenceById(request.doctorID);

        LocalDate startDate = request.date;
        LocalDate stopDate = startDate.plusDays(1);


        if (spec == null && doc == null) {
            return talonRepo.findAvailableTickets(
                    LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                    LocalDateTime.of(stopDate, LocalTime.MIDNIGHT)
            ).stream().map(this::mapTalonToDto).toList();
        }

        if (spec != null && doc != null) {
            return talonRepo.findAvailableTickets(
                    LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                    LocalDateTime.of(stopDate, LocalTime.MIDNIGHT),
                    doc,
                    spec
            ).stream().map(this::mapTalonToDto).toList();
        }
        if (doc != null) {
            return talonRepo.findAvailableTickets(
                    LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                    LocalDateTime.of(stopDate, LocalTime.MIDNIGHT),
                    doc
            ).stream().map(this::mapTalonToDto).toList();
        }
        if (spec != null) {
            return talonRepo.findAvailableTickets(
                    LocalDateTime.of(startDate, LocalTime.MIDNIGHT),
                    LocalDateTime.of(stopDate, LocalTime.MIDNIGHT),
                    spec
            ).stream().map(this::mapTalonToDto).toList();
        }

        return new ArrayList<>();

//        return talonRepo.findAvailableTicketDatesDocTest(LocalDate.of(2024, 3, 2), doc)
//                .stream().map(
//                        el-> TalonResponse.builder()
//                                .id(el.getUuid())
//                                .doctorId(el.getDoctor() == null ? el.getDoctor().getUuid() : null)
//                                .specializationId(el.getSpecialization().getUuid())
//                                .startDateTime(el.getStartDateTime())
//                                .stopDateTime(el.getStopDateTime())
//                                .build()
//                ).toList();
    }

    @PostMapping("applyfor")
    public boolean applyForTalon(UUID talonUuid, UUID patientUuid) {
        var talon = talonRepo.findById(talonUuid);
        var patient = patientRepo.findById(patientUuid);
        if (talon.isEmpty() || patient.isEmpty())
            return false;
        talon.get().setPatient(patient.get());
        return true;
    }

    @PostMapping("getmytalons")
    public List<TalonResponse> getPatientTalons(UUID patientUuid) {
        return talonRepo.findAllByPatientUuid(patientUuid).stream().map(this::mapTalonToDto).toList();
    }

    private TalonResponse mapTalonToDto(Talon talon) {
        return TalonResponse.builder()
                .id(talon.getUuid())
                .doctorId(talon.getDoctor() != null ? talon.getDoctor().getUuid() : null)
                .specializationId(talon.getSpecialization().getUuid())
                .startDateTime(talon.getStartDateTime())
                .stopDateTime(talon.getStopDateTime())
                .build();
    }
}
