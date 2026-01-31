package com.insurance.auto.domain.model;

import java.time.LocalDate;
import java.time.Period;

public record Driver(
        Long id,
        String name,
        LocalDate birthDate,
        int accidentHistoryCount
) {
    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
