package com.insurance.auto.adapter.out.persistence.repository;

import com.insurance.auto.adapter.out.persistence.entity.PolicyJpaEntity;
import com.insurance.auto.domain.model.PolicyStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
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

        PolicyJpaEntity oldPolicy = new PolicyJpaEntity(
                UUID.randomUUID().toString(),
                driverId,
                10L,
                500000L,
                PolicyStatus.APPROVED.name(),
                LocalDate.now().minusYears(2),
                LocalDate.now().minusYears(1)
        );
        policyRepository.save(oldPolicy);

        PolicyJpaEntity newPolicy = new PolicyJpaEntity(
                UUID.randomUUID().toString(),
                driverId,
                20L,
                600000L,
                PolicyStatus.APPROVED.name(),
                LocalDate.now(),
                LocalDate.now().plusYears(1)
        );
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