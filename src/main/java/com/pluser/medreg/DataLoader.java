package com.pluser.medreg;

import com.pluser.medreg.domain.entity.Doctor;
import com.pluser.medreg.domain.entity.Specialization;
import com.pluser.medreg.domain.repo.IDoctorRepo;
import com.pluser.medreg.domain.repo.ISpecializationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final ISpecializationRepo specializationRepo;
    private final IDoctorRepo doctorRepo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Stream.of("Общий", "Стоматолог", "Хирург").forEach(
                specName -> {
                    var optSpec = specializationRepo.findByName(specName);
                    if (optSpec.isEmpty()) {
                        var newSpec = new Specialization();
                        newSpec.setName(specName);
                        specializationRepo.save(newSpec);
                    }
                }
        );
        Stream.of("Иван", "Светлана", "Юрий").forEach(
                doctorName -> {
                    var optDoc = doctorRepo.findDoctorByFirstName(doctorName);
                    if (optDoc.isEmpty()) {
                        var newDoc = new Doctor();
                        newDoc.setFirstName(doctorName);
                        doctorRepo.save(newDoc);
                    }
                }
        );

        Map<String, List<String>> userSpec = new HashMap<>() {{
            put("Иван", new ArrayList<String>() {{
                add("Хирург"); add("Общий");
            }});
            put("Светлана", new ArrayList<String>() {{
                add("Общий"); add("Стоматолог");
            }});
            put("Юрий", new ArrayList<String>() {{
                add("Стоматолог"); add("Хирург");
            }});
        }};

        userSpec.forEach((key, value) -> {
            var optDoc = doctorRepo.findDoctorByFirstName(key);
            var specs = specializationRepo.findAllByNameIn(value);
            if (optDoc.isPresent()) {
                var doc = optDoc.get();
                doc.setSpecializations(specs);
                doctorRepo.save(doc);
            }
        });
    }
}
