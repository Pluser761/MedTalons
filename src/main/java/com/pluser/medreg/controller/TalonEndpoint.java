package com.pluser.medreg.controller;

import com.pluser.medreg.controller.xmlpayload.TalonGenRequest;
import com.pluser.medreg.controller.xmlpayload.TalonGenResponse;
import com.pluser.medreg.domain.entity.Specialization;
import com.pluser.medreg.domain.entity.Talon;
import com.pluser.medreg.domain.repo.ISpecializationRepo;
import com.pluser.medreg.service.TalonService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Endpoint
public class TalonEndpoint {
    private static final String NAMESPACE_URI = "http://pluser.com/medreg";

    private final TalonService talonService;
    // не стал реализовывать сервис из соображений отсутствия бизнес логики в слое
    private final ISpecializationRepo specializationRepo;

    public TalonEndpoint(TalonService talonService, ISpecializationRepo specializationRepo) {
        this.talonService = talonService;
        this.specializationRepo = specializationRepo;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "talonGenRequest")
    @ResponsePayload
    public TalonGenResponse generateTalons(@RequestPayload TalonGenRequest request) {
        // обработка длительностей специализаций генерируемых слотов(талонов)
        List<Specialization> specializationList = this.specializationRepo.findAll();
        var specDur = request.getSpecializationDurations();
        if (specDur != null) {
            var specList = specDur.getDurationPair();
            if (!specList.isEmpty()) {
                // Билд мапы измененных специализаций слота для последующего применения к specializationList
                Map<UUID, Duration> durations = specList.stream().collect(
                        Collectors.toMap(
                                element -> UUID.fromString(element.getSpecializationUuid()),
                                element -> Duration.parse(element.getPeriod().toString())
                        )
                );
                for (var spec : specializationList) {
                    if (durations.containsKey(spec.getUuid())) {
                        spec.setDuration(durations.get(spec.getUuid()));
                    }
                }
            }
        }

        // обработка промежутка времени генерируемых слотов(талонов)
        var datesRule = request.getGeneral().getDatesRule();

        LocalDateTime start = LocalDateTime.from(datesRule.getStartDate().toGregorianCalendar().toZonedDateTime());
        LocalDateTime stop;

        if (datesRule.getStopDate() != null) {
            stop = LocalDateTime.from(datesRule.getStopDate().toGregorianCalendar().toZonedDateTime());
        } else {
            Duration duration = Duration.parse(datesRule.getPeriod().toString());
            stop = start.plus(duration);
        }

        // обработка специализаций генерируемых слотов(талонов)
        var specRule = request.getGeneral().getSpecializationRule();
        if (specRule != null) {
            List<UUID> specUuids = specRule.getSpecializationUuid().stream()
                    .map(UUID::fromString)
                    .toList();
            // убираем специализации не указанные в XML
            specializationList.removeIf(spec -> !specUuids.contains(spec.getUuid()));
        }

        if (request.getGeneral().isDeleteInPeriod())
            talonService.deleteTalons(start, stop);
        List<Talon> createdTalons = talonService.generateTalons(start, stop, specializationList);

        TalonGenResponse response = new TalonGenResponse();
        response.setStatus("Ok");
        var talons = response.getTalon();

        talons.addAll(
                // По хорошему конечно найти DTO решение, но можно и так помапить через stream api lambda
                createdTalons.stream().map(el -> {
                    var xmlPatient = new com.pluser.medreg.controller.xmlpayload.Patient();
                    var patient = el.getPatient();

                    if (patient != null) {
                        xmlPatient.setUuid(patient.getUuid().toString());
                        xmlPatient.setFirstName(patient.getFirstName());
                    }

                    var xmlDoctor = new com.pluser.medreg.controller.xmlpayload.Doctor();
                    var doctor = el.getDoctor();

                    if (doctor != null) {
                        xmlDoctor.setUuid(doctor.getUuid().toString());
                        xmlDoctor.setFirstName(doctor.getFirstName());
                    }

                    var xmlSpec = new com.pluser.medreg.controller.xmlpayload.Specialization();
                    var specialization = el.getSpecialization();

                    if (specialization != null) {
                        xmlSpec.setUuid(specialization.getUuid().toString());
                        xmlSpec.setName(specialization.getName());
                    }

                    var xmlTalon = new com.pluser.medreg.controller.xmlpayload.Talon();

                    xmlTalon.setUuid(el.getUuid().toString());
                    xmlTalon.setDoctor(xmlDoctor);
                    xmlTalon.setPatient(xmlPatient);
                    xmlTalon.setSpecialization(xmlSpec);
                    try {
                        xmlTalon.setStartDateTime(
                                DatatypeFactory.newInstance().newXMLGregorianCalendar(
                                        el.getStartDateTime().format(DateTimeFormatter.ISO_DATE_TIME)
                                ));
                        xmlTalon.setStopDateTime(
                                DatatypeFactory.newInstance().newXMLGregorianCalendar(
                                        el.getStopDateTime().format(DateTimeFormatter.ISO_DATE_TIME)
                                ));
                    } catch (DatatypeConfigurationException ignored) {

                    }
                    return xmlTalon;
                }).toList()
        );
        return response;
    }
}