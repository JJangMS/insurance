package com.insurance.auto.application.service;

import com.insurance.auto.application.port.in.RegisterPolicyCommand;
import com.insurance.auto.application.port.in.RegisterPolicyUseCase;
import com.insurance.auto.application.port.out.LoadInsuredPort;
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

    private final LoadInsuredPort loadInsuredPort;
    private final SavePolicyPort savePolicyPort;

    @Override
    public Policy register(RegisterPolicyCommand command) {
        Driver driver = loadInsuredPort.loadDriver(command.driverId())
                .orElseThrow(() -> new IllegalArgumentException("첫 가입 운전자입니다."));
        Car car = loadInsuredPort.loadCar(command.carId())
                .orElseThrow(() -> new IllegalArgumentException("첫 가입 차량입니다."));

        Policy policy = new Policy(driver.id(), car.id());

        policy.underwrite(driver);

        savePolicyPort.save(policy);

        return policy;
    }
}
