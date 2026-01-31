package com.insurance.auto.application.port.in;

import jakarta.validation.constraints.NotNull;

public record RegisterPolicyCommand(
        @NotNull Long driverId,
        @NotNull Long carId
) {
    public RegisterPolicyCommand {
        if (driverId == null || carId == null) {
            throw new IllegalArgumentException("Driver ID와 Car ID는 필수입니다.");
        }
    }
}
