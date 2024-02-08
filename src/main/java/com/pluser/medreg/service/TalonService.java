package com.pluser.medreg.service;

import com.pluser.medreg.domain.entity.Specialization;
import com.pluser.medreg.domain.entity.Talon;
import com.pluser.medreg.domain.repo.IDoctorRepo;
import com.pluser.medreg.domain.repo.ITalonRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class TalonService {
    private final ITalonRepo talonRepo;
    private final IDoctorRepo doctorRepo;


    public TalonService(ITalonRepo talonRepo, IDoctorRepo doctorRepo) {
        this.talonRepo = talonRepo;
        this.doctorRepo = doctorRepo;
    }

    public List<Talon> generateTalons(LocalDateTime start, LocalDateTime stop, List<Specialization> specializationList) {

        List<Talon> newTalons = new LinkedList<>();

        for (var spec : specializationList) {
            var talonDuration = spec.getDuration();

            for (LocalDateTime currentTalonStart = start;
                 currentTalonStart.isBefore(stop.minus(talonDuration)) || currentTalonStart.isEqual(stop.minus(talonDuration));
                 currentTalonStart = currentTalonStart.plus(talonDuration)) {
                var newTalon = new Talon();
                newTalon.setSpecialization(spec);
                newTalon.setStartDateTime(currentTalonStart);
                newTalon.setStopDateTime(currentTalonStart.plus(talonDuration));
                var notAvailableDoctors = doctorRepo.findDoctorsWithInterceptedTickets(newTalon.getStartDateTime(), newTalon.getStopDateTime(), spec);
                var specializedDoctors = doctorRepo.findDoctorsBySpecializationsContaining(spec);
                var availableDoctors = specializedDoctors.stream()
                        .filter(doctor -> !notAvailableDoctors.contains(doctor)).toList();
                if (!availableDoctors.isEmpty()) {
                    newTalon.setDoctor(availableDoctors.get(0));
                }

                newTalons.add(newTalon);
            }
        }

        return talonRepo.saveAll(newTalons);
    }

    @Transactional
    public void deleteTalons(LocalDateTime start, LocalDateTime stop) {
        talonRepo.deleteTalonsByStartDateTimeGreaterThanEqualAndStopDateTimeLessThanEqual(start, stop);
    }
}
