package com.insurance.auto.application.service;

import com.insurance.auto.application.port.in.RegisterPolicyCommand;
import com.insurance.auto.application.port.in.RegisterPolicyUseCase;
import com.insurance.auto.application.port.in.dto.CustomerInquiryInfo;
import com.insurance.auto.application.port.out.LoadCarPort;
import com.insurance.auto.application.port.out.LoadDriverPort;
import com.insurance.auto.application.port.out.LoadPolicyPort;
import com.insurance.auto.application.port.out.SavePolicyPort;
import com.insurance.auto.domain.model.Car;
import com.insurance.auto.domain.model.Driver;
import com.insurance.auto.domain.model.Policy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AutoPolicyService implements RegisterPolicyUseCase {

    private final LoadCarPort loadCarPort;
    private final LoadDriverPort loadDriverPort;
    private final LoadPolicyPort loadPolicyPort;
    private final SavePolicyPort savePolicyPort;

    @Override
    public Policy create(RegisterPolicyCommand command) {
        Driver driver = loadDriverPort.loadDriver(command.driverId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 운전자입니다."));
        Car car = loadCarPort.loadCar(command.carId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가입 차량입니다."));

        Policy policy = new Policy(driver.id(), car.id());

        long basePremium = 600_000L;
        long ageSurcharge = 200_000L;
        long accidentSurcharge = 100_000L;
        policy.review(driver, basePremium, ageSurcharge, accidentSurcharge);

        return savePolicyPort.save(policy);
    }

    public Policy approve(Long policyId, java.time.LocalDate requestedStartDate) {
        Policy policy = loadPolicyPort.loadPolicy(policyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가입 내역을 찾을 수 없습니다."));

        policy.approve(requestedStartDate);

        return savePolicyPort.save(policy);
    }

    @Transactional(readOnly = true)
    public CustomerInquiryInfo inquireCustomer(String name, LocalDate birthDate, String phone) {
        return loadDriverPort.loadDriver(name, birthDate, phone)
                .map(driver -> {
                    Optional<Car> car = loadPolicyPort.loadLatestPolicy(driver.id())
                            .flatMap(policy -> loadCarPort.loadCar(policy.getCarId()));

                    return new CustomerInquiryInfo(driver, car.orElse(null));
                })
                .orElseThrow(() -> new IllegalArgumentException("가입 정보를 찾을 수 없습니다."));
    }
}
