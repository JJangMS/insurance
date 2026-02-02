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
        PolicyJpaEntity entity;

        if (policy.getId() == null) {
            entity = PolicyJpaEntity.from(policy);
        } else {
            entity = policyRepository.findById(policy.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID 입니다."));

            entity.updatePolicy(policy);
        }

        return mapToDomain(policyRepository.save(entity));
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
