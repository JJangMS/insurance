package com.insurance.auto.application.service;

import com.insurance.auto.application.port.in.RegisterPolicyCommand;
import com.insurance.auto.application.port.in.dto.CustomerInquiryInfo;
import com.insurance.auto.application.port.out.*;
import com.insurance.auto.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AutoPolicyServiceTest {

    @InjectMocks AutoPolicyService autoPolicyService;
    @Mock LoadDriverPort loadDriverPort;
    @Mock LoadCarPort loadCarPort;
    @Mock SavePolicyPort savePolicyPort;
    @Mock LoadPolicyPort loadPolicyPort;

    @Test
    @DisplayName("운전자와 차량 정보가 있으면 임시 정책을 생성하고 저장한다")
    void register_success() {
        // Given
        given(loadDriverPort.loadDriver(1L)).willReturn(Optional.of(new Driver(1L, "장민수", LocalDate.now(), 0, "010-1234-5678")));
        given(loadCarPort.loadCar(1L)).willReturn(Optional.of(new Car(1L, "234오6789", "그랜저", "더 뉴 그랜저IG 2.5 가솔린", 2021, 19_390_000L, true)));

        // When
        Policy result = autoPolicyService.create(new RegisterPolicyCommand(1L, 1L));

        // Then
        assertThat(result.getStatus()).isEqualTo(PolicyStatus.REVIEWING);
        verify(savePolicyPort).save(any(Policy.class));
    }

    @Test
    @DisplayName("고객 조회 시 정보가 일치하면 운전자와 차량 정보를 반환한다")
    void inquire_customer_success() {
        // Given
        Driver driver = new Driver(1L, "장민수", LocalDate.of(1993, 2, 12), 0, "010-1234-5678");
        Policy policy = new Policy(100L, "P-123", 1L, 1L, 500000L, PolicyStatus.APPROVED, null, null);
        Car car = new Car(1L, "234오6789", "그랜저", "더 뉴 그랜저IG 2.5 가솔린", 2021, 19_390_000L, true);

        given(loadDriverPort.loadDriver("장민수", LocalDate.of(1993, 2, 12), "010-1234-5678"))
                .willReturn(Optional.of(driver));
        given(loadPolicyPort.loadLatestPolicy(1L)).willReturn(Optional.of(policy));
        given(loadCarPort.loadCar(1L)).willReturn(Optional.of(car));

        // When
        CustomerInquiryInfo info = autoPolicyService.inquireCustomer("장민수", LocalDate.of(1993, 2, 12), "010-1234-5678");

        // Then
        assertThat(info.driver()).isEqualTo(driver);
        assertThat(info.car()).isEqualTo(car);
    }

    @Test
    @DisplayName("정책 승인 시 상태가 APPROVED로 변경되고 저장된다")
    void approve_policy_success() {
        // Given
        Policy policy = new Policy(1L, 1L);
        Driver driver = new Driver(1L, "장민수", LocalDate.of(1993, 2, 12), 0, "010-1234-5678");

        given(loadPolicyPort.loadPolicy(1L)).willReturn(Optional.of(policy));
        given(loadDriverPort.loadDriver(1L)).willReturn(Optional.of(driver));

        // When
        autoPolicyService.approve(1L, LocalDate.now());

        // Then
        assertThat(policy.getStatus()).isEqualTo(PolicyStatus.APPROVED);
        verify(savePolicyPort).save(policy);
    }
}
