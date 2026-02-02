package com.insurance.auto.adapter.out.persistence;

import com.insurance.auto.adapter.out.persistence.entity.PolicyJpaEntity;
import com.insurance.auto.adapter.out.persistence.repository.PolicyJpaRepository;
import com.insurance.auto.application.port.out.SavePolicyPort;
import com.insurance.auto.domain.model.Policy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PolicyPersistenceAdapter implements SavePolicyPort {

    private final PolicyJpaRepository policyRepository;

    @Override
    public void save(Policy policy) {
        PolicyJpaEntity entity = new PolicyJpaEntity(
                policy.getPolicyNumber(),
                policy.getDriverId(),
                policy.getCarId(),
                policy.getPremium(),
                policy.getStatus().name()
        );
        policyRepository.save(entity);
    }
}
