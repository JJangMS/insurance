package com.insurance.auto.application.port.in;

import com.insurance.auto.domain.model.Policy;

public interface RegisterPolicyUseCase {
    Policy register(RegisterPolicyCommand command);
}
