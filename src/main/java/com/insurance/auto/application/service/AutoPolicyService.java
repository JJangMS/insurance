package com.insurance.auto.application.service;

import com.insurance.auto.application.port.in.RegisterPolicyCommand;
import com.insurance.auto.application.port.in.RegisterPolicyUseCase;
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

        savePolicyPort.save(policy);

        return policy;
    }

    public Policy approve(Long policyId, java.time.LocalDate requestedStartDate) {
        Policy policy = loadPolicyPort.loadPolicy(policyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가입 내역을 찾을 수 없습니다."));
        Driver driver = loadDriverPort.loadDriver(policy.getDriverId())
                .orElseThrow(() -> new IllegalArgumentException("운전자 정보를 불러올 수 없습니다."));

        Long calculatedPremium = dummyCalculatePremium(driver);

        policy.approve(driver, requestedStartDate, calculatedPremium);

        savePolicyPort.save(policy);

        return policy;
    }

    private Long dummyCalculatePremium(Driver driver) {
        long basePremium = 600_000L;

        if (driver.getAge() < 25) {
            basePremium += 200_000L;
        }

        basePremium += (driver.accidentHistoryCount() * 100_000L);

        return basePremium;
    }
}
