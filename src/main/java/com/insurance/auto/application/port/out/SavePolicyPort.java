package com.insurance.auto.application.port.out;

import com.insurance.auto.domain.model.Policy;

public interface SavePolicyPort {
    Policy save(Policy policy);
}
