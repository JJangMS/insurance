package com.insurance.auto.adapter.out.persistence.repository;

import com.insurance.auto.adapter.out.persistence.entity.PolicyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolicyJpaRepository extends JpaRepository<PolicyJpaEntity, Long> {
    Optional<PolicyJpaEntity> findTopByDriverIdOrderByIdDesc(Long driverId);
}
