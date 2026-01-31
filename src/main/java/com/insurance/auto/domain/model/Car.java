package com.insurance.auto.domain.model;

public record Car(
        Long id,
        String carNumber,
        String modelName,
        Long price,
        boolean hasBlackBox
) {
}
