package com.insurance.auto.adapter.out.persistence.repository;

import com.insurance.auto.adapter.out.persistence.entity.PolicyJpaEntity;
import com.insurance.auto.domain.model.PolicyStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PolicyJpaRepositoryTest {

    @Autowired
    PolicyJpaRepository policyRepository;

    @Test
    @DisplayName("특정 운전자의 가장 최근 계약을 찾는다")
    void find_latest_policy_by_driver() {
        // Given
        Long driverId = 1L;

        PolicyJpaEntity oldPolicy = PolicyJpaEntity.builder()
                .policyNumber(UUID.randomUUID().toString())
                .driverId(driverId)
                .carId(10L)
                .premium(500000L)
                .status(PolicyStatus.APPROVED.name())
                .startDate(LocalDate.now().minusYears(2))
                .endDate(LocalDate.now().minusYears(1))
                .createdAt(LocalDateTime.now().minusYears(2))
                .build();
        policyRepository.save(oldPolicy);

        PolicyJpaEntity newPolicy = PolicyJpaEntity.builder()
                .policyNumber(UUID.randomUUID().toString())
                .driverId(driverId)
                .carId(20L)
                .premium(600000L)
                .status(PolicyStatus.APPROVED.name())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusYears(1))
                .createdAt(LocalDateTime.now())
                .build();
        policyRepository.save(newPolicy);

        // When
        Optional<PolicyJpaEntity> result = policyRepository.findTopByDriverIdOrderByIdDesc(driverId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCarId()).isEqualTo(20L);
        assertThat(result.get().getPremium()).isEqualTo(600000L);
    }

    @Test
    @DisplayName("계약 내역이 없으면 빈 값을 반환한다")
    void find_latest_policy_not_found() {
        // Given
        Long driverId = 999L;

        // When
        Optional<PolicyJpaEntity> result = policyRepository.findTopByDriverIdOrderByIdDesc(driverId);

        // Then
        assertThat(result).isEmpty();
    }
}