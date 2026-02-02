package com.insurance.auto.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class PolicyTest {

    @Test
    @DisplayName("정책 생성 시 초기 상태는 REVIEWING이어야 한다")
    void create_policy_initial_status() {
        Policy policy = new Policy(1L, 1L);
        assertThat(policy.getStatus()).isEqualTo(PolicyStatus.REVIEWING);
    }

    @Test
    @DisplayName("만 21세 미만 운전자는 가입이 거절된다")
    void underwrite_reject_young_driver() {
        // Given
        Policy policy = new Policy(1L, 1L);
        Driver youngDriver = new Driver(1L, "어린이", LocalDate.now().minusYears(20), 0, "010-1234-5678");

        // When
        policy.approve(youngDriver, LocalDate.now(), 500000L);

        // Then
        assertThat(policy.getStatus()).isEqualTo(PolicyStatus.REJECTED);
    }

    @Test
    @DisplayName("사고 이력이 3회 이상이면 가입이 거절된다")
    void underwrite_reject_accident_history() {
        // Given
        Policy policy = new Policy(1L, 1L);
        Driver accidentDriver = new Driver(1L, "사고뭉치", LocalDate.now().minusYears(30), 3, "010-1234-5678");

        // When
        policy.approve(accidentDriver, LocalDate.now(), 500000L);

        // Then
        assertThat(policy.getStatus()).isEqualTo(PolicyStatus.REJECTED);
    }

    @Test
    @DisplayName("정상 운전자는 승인되며 기간과 보험료가 확정된다")
    void approve_success() {
        // Given
        Policy policy = new Policy(1L, 1L);
        Driver goodDriver = new Driver(1L, "베스트드라이버", LocalDate.now().minusYears(30), 0, "010-1234-5678");
        LocalDate startDate = LocalDate.of(2026, 2, 2);
        Long premium = 800_000L;

        // When
        policy.approve(goodDriver, startDate, premium);

        // Then
        assertThat(policy.getStatus()).isEqualTo(PolicyStatus.APPROVED);
        assertThat(policy.getStartDate()).isEqualTo(startDate);
        assertThat(policy.getEndDate()).isEqualTo(startDate.plusYears(1).minusDays(1));
        assertThat(policy.getPremium()).isEqualTo(premium);
    }
}
