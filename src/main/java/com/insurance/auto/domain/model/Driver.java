package com.insurance.auto.domain.model;

import java.time.LocalDate;

public record Driver(
        Long id,
        String name,
        LocalDate birthDate,
        int accidentHistoryCount
) {
}
