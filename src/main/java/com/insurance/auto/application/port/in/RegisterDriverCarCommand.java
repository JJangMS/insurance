package com.insurance.auto.application.port.in;

import java.time.LocalDate;

public record RegisterDriverCarCommand(
        String name,
        LocalDate birthDate,
        String phone,
        String carNumber,
        String carModel,
        String carModelName,
        int carModelYear
) {}
