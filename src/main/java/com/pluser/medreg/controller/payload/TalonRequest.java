package com.pluser.medreg.controller.payload;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class TalonRequest {
    public UUID doctorID;
    public UUID specializationID;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    public LocalDate date;
}
