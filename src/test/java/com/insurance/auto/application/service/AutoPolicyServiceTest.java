package com.insurance.auto.application.service;

import com.insurance.auto.application.port.in.RegisterDriverCarCommand;
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
    @Mock LoadPolicyPort loadPolicyPort;
    @Mock SaveDriverPort saveDriverPort;
    @Mock SaveCarPort saveCarPort;
    @Mock SavePolicyPort savePolicyPort;

    @Test
    @DisplayName("운전자와 차량 정보가 있으면 임시 정책을 생성하고 저장한다")
    void register_success() {
        // Given
        given(loadDriverPort.loadDriver(1L)).willReturn(Optional.of(new Driver(1L, "장민수", LocalDate.of(1993, 2, 12), 0, "010-1234-5678")));
        given(loadCarPort.loadCar(1L)).willReturn(Optional.of(new Car(1L, "234오6789", "그랜저", "더 뉴 그랜저IG 2.5 가솔린", 2021, 19_390_000L, true)));
        given(savePolicyPort.save(any(Policy.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        Policy result = autoPolicyService.create(new RegisterPolicyCommand(1L, 1L));

        // Then
        assertThat(result.getStatus()).isEqualTo(PolicyStatus.REVIEWING);
        assertThat(result.getPremium()).isEqualTo(600_000L);
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

        policy.review(driver, 600_000L, 200_000L, 100_000L);

        given(loadPolicyPort.loadPolicy(1L)).willReturn(Optional.of(policy));
        given(savePolicyPort.save(any(Policy.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        autoPolicyService.approve(1L, LocalDate.now());

        // Then
        assertThat(policy.getStatus()).isEqualTo(PolicyStatus.APPROVED);
        verify(savePolicyPort).save(policy);
    }

    @Test
    @DisplayName("만 25세 미만 운전자는 연령 할증(20만원)이 추가된다")
    void register_with_age_surcharge() {
        // Given
        Driver youngDriver = new Driver(1L, "젊은이", LocalDate.now().minusYears(24), 0, "010-1234-5678");
        Car car = new Car(1L, "234오6789", "그랜저", "더 뉴 그랜저IG", 2021, 19_390_000L, true);

        given(loadDriverPort.loadDriver(1L)).willReturn(Optional.of(youngDriver));
        given(loadCarPort.loadCar(1L)).willReturn(Optional.of(car));
        given(savePolicyPort.save(any(Policy.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        Policy result = autoPolicyService.create(new RegisterPolicyCommand(1L, 1L));

        // Then
        assertThat(result.getPremium()).isEqualTo(800_000L);
    }

    @Test
    @DisplayName("사고 이력이 2회 있으면 사고 할증(20만원)이 추가된다")
    void register_with_accident_surcharge() {
        // Given
        Driver accidentDriver = new Driver(1L, "덜사고뭉치", LocalDate.now().minusYears(30), 2, "010-1234-5678");
        Car car = new Car(1L, "234오6789", "그랜저", "더 뉴 그랜저IG", 2021, 19_390_000L, true);

        given(loadDriverPort.loadDriver(1L)).willReturn(Optional.of(accidentDriver));
        given(loadCarPort.loadCar(1L)).willReturn(Optional.of(car));
        given(savePolicyPort.save(any(Policy.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        Policy result = autoPolicyService.create(new RegisterPolicyCommand(1L, 1L));

        // Then
        assertThat(result.getPremium()).isEqualTo(800_000L);
    }

    @Test
    @DisplayName("만 21세 미만 운전자는 가입이 거절된다")
    void register_rejected_too_young() {
        // Given
        Driver babyDriver = new Driver(1L, "어린이", LocalDate.now().minusYears(20), 0, "010-1234-5678");
        Car car = new Car(1L, "234오6789", "그랜저", "더 뉴 그랜저IG", 2021, 19_390_000L, true);

        given(loadDriverPort.loadDriver(1L)).willReturn(Optional.of(babyDriver));
        given(loadCarPort.loadCar(1L)).willReturn(Optional.of(car));
        given(savePolicyPort.save(any(Policy.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        Policy result = autoPolicyService.create(new RegisterPolicyCommand(1L, 1L));

        // Then
        assertThat(result.getStatus()).isEqualTo(PolicyStatus.REJECTED);
        assertThat(result.getPremium()).isEqualTo(0L);
    }

    @Test
    @DisplayName("신규 가입 요청 시 운전자와 차량 정보를 저장하고 반환한다")
    void register_new_driver_and_car_success() {
        // Given
        RegisterDriverCarCommand command = new RegisterDriverCarCommand(
                "장민수수", LocalDate.of(1995, 5, 5), "010-9999-8888",
                "123가4567", "모델y", "듀얼모터", 2026
        );

        // Mocking: 저장 후 ID가 부여된 객체를 반환한다고 가정
        Driver savedDriver = new Driver(10L, command.name(), command.birthDate(), 0, command.phone());
        Car savedCar = new Car(20L, command.carNumber(), command.carModel(), command.carModelName(), command.carModelYear(), 20_000_000L, true);

        given(saveDriverPort.save(any(Driver.class))).willReturn(savedDriver);
        given(saveCarPort.save(any(Car.class))).willReturn(savedCar);

        // When
        CustomerInquiryInfo result = autoPolicyService.register(command);

        // Then
        assertThat(result.driver()).isEqualTo(savedDriver);
        assertThat(result.car()).isEqualTo(savedCar);

        // 실제로 저장이 호출되었는지 검증
        verify(saveDriverPort).save(any(Driver.class));
        verify(saveCarPort).save(any(Car.class));
    }
}
