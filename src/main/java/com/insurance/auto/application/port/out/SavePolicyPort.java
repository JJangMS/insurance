package com.insurance.auto.application.port.out;

import com.insurance.auto.domain.model.Policy;

public interface SavePolicyPort {
    void save(Policy policy);
}
