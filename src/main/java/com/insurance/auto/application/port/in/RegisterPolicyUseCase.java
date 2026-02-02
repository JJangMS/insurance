package com.insurance.auto.application.port.in;

import com.insurance.auto.domain.model.Policy;

import java.time.LocalDate;

public interface RegisterPolicyUseCase {
    Policy create(RegisterPolicyCommand command);

    Policy approve(Long policyId, LocalDate startDate);
}
