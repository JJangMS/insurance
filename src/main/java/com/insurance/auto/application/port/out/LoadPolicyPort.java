package com.insurance.auto.application.port.out;

import com.insurance.auto.domain.model.Policy;
import java.util.Optional;

public interface LoadPolicyPort {
    Optional<Policy> loadPolicy(Long policyId);
}
