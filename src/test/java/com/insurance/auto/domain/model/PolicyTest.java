package com.insurance.auto.domain.model;

import com.insurance.auto.domain.exception.RejectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PolicyTest {

    private static final long BASE_PREMIUM = 600_000L;
    private static final long AGE_SURCHARGE = 200_000L;
    private static final long ACCIDENT_SURCHARGE = 100_000L;

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

        // When, Then
        assertThatThrownBy(() ->
                policy.review(youngDriver, BASE_PREMIUM, AGE_SURCHARGE, ACCIDENT_SURCHARGE)
        ).isInstanceOf(RejectException.class)
                .hasMessageContaining("죄송합니다. 만 21세 미만은 가입이 불가능합니다.");
    }

    @Test
    @DisplayName("사고 이력이 3회 이상이면 가입이 거절된다")
    void underwrite_reject_accident_history() {
        // Given
        Policy policy = new Policy(1L, 1L);
        Driver accidentDriver = new Driver(1L, "사고뭉치", LocalDate.now().minusYears(30), 3, "010-1234-5678");

        // When, Then
        assertThatThrownBy(() ->
                policy.review(accidentDriver, BASE_PREMIUM, AGE_SURCHARGE, ACCIDENT_SURCHARGE)
        ).isInstanceOf(RejectException.class)
                .hasMessageContaining("최근 사고 이력이 많아(3회 이상) 인수가 거절되었습니다.");
    }

    @Test
    @DisplayName("25세 미만 운전자는 연령 할증이 포함된 보험료가 책정되고 REVIEWING 상태가 된다")
    void review_success_with_age_surcharge() {
        // Given
        Policy policy = new Policy(1L, 1L);
        Driver youngDriver = new Driver(1L, "젊은이", LocalDate.now().minusYears(24), 0, "010-1234-5678");

        // When
        policy.review(youngDriver, BASE_PREMIUM, AGE_SURCHARGE, ACCIDENT_SURCHARGE);

        // Then
        assertThat(policy.getStatus()).isEqualTo(PolicyStatus.REVIEWING);
        assertThat(policy.getPremium()).isEqualTo(BASE_PREMIUM + AGE_SURCHARGE);
    }

    @Test
    @DisplayName("심사가 완료된 정책은 승인 가능하며, 기간과 증권번호가 확정된다")
    void approve_success() {
        // Given
        Policy policy = new Policy(1L, 1L);
        Driver driver = new Driver(1L, "베스트드라이버", LocalDate.now().minusYears(30), 0, "010-1234-5678");

        policy.review(driver, BASE_PREMIUM, AGE_SURCHARGE, ACCIDENT_SURCHARGE);

        LocalDate startDate = LocalDate.of(2026, 2, 2);

        // When
        policy.approve(startDate);

        // Then
        assertThat(policy.getStatus()).isEqualTo(PolicyStatus.APPROVED);
        assertThat(policy.getStartDate()).isEqualTo(startDate);
        assertThat(policy.getEndDate()).isEqualTo(startDate.plusYears(1).minusDays(1));
        assertThat(policy.getPolicyNumber()).isNotNull();
    }
}
