package com.insurance.auto.domain.model;

public record Car(
        Long id,
        String carNumber,
        String modelName,
        String subModelName,
        int modelYear,
        Long price,
        boolean hasBlackBox
) {
}
