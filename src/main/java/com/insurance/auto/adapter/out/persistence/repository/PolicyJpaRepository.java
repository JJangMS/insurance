package com.insurance.auto.adapter.out.persistence.repository;

import com.insurance.auto.adapter.out.persistence.entity.PolicyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyJpaRepository extends JpaRepository<PolicyJpaEntity, Long> {
}
