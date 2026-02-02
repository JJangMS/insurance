package com.insurance.auto.adapter.out.persistence;

import com.insurance.auto.adapter.out.persistence.entity.PolicyJpaEntity;
import com.insurance.auto.adapter.out.persistence.repository.PolicyJpaRepository;
import com.insurance.auto.application.port.out.LoadPolicyPort;
import com.insurance.auto.application.port.out.SavePolicyPort;
import com.insurance.auto.domain.model.Policy;
import com.insurance.auto.domain.model.PolicyStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PolicyPersistenceAdapter implements SavePolicyPort, LoadPolicyPort {

    private final PolicyJpaRepository policyRepository;

    @Override
    public Policy save(Policy policy) {
        PolicyJpaEntity entity = new PolicyJpaEntity(
                policy.getPolicyNumber(),
                policy.getDriverId(),
                policy.getCarId(),
                policy.getPremium(),
                policy.getStatus().name(),
                policy.getStartDate(),
                policy.getEndDate()
        );
        PolicyJpaEntity savedEntity = policyRepository.save(entity);

        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<Policy> loadPolicy(Long policyId) {
        return policyRepository.findById(policyId)
                .map(this::mapToDomain);
    }

    @Override
    public Optional<Policy> loadLatestPolicy(Long driverId) {
        return policyRepository.findTopByDriverIdOrderByIdDesc(driverId)
                .map(this::mapToDomain);
    }

    private Policy mapToDomain(PolicyJpaEntity entity) {
        return new Policy(
                entity.getId(),
                entity.getPolicyNumber(),
                entity.getDriverId(),
                entity.getCarId(),
                entity.getPremium(),
                PolicyStatus.valueOf(entity.getStatus()),
                entity.getStartDate(),
                entity.getEndDate()
        );
    }
}
