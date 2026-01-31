package com.insurance.auto.adapter.out.persistence;

import com.insurance.auto.adapter.out.persistence.entity.PolicyJpaEntity;
import com.insurance.auto.adapter.out.persistence.repository.PolicyJpaRepository;
import com.insurance.auto.application.port.out.LoadInsuredPort;
import com.insurance.auto.application.port.out.SavePolicyPort;
import com.insurance.auto.domain.model.Car;
import com.insurance.auto.domain.model.Driver;
import com.insurance.auto.domain.model.Policy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PolicyPersistenceAdapter implements LoadInsuredPort, SavePolicyPort {

    private final PolicyJpaRepository policyRepository;

    @Override
    public Optional<Driver> loadDriver(Long driverId) {
        // Mock Data
        if (driverId == 1L) {
            return Optional.of(new Driver(1L, "장민수", LocalDate.of(1995, 1, 1), 0));
        } else if (driverId == 2L) {
            return Optional.of(new Driver(2L, "어린친구", LocalDate.of(2005, 1, 1), 5));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Car> loadCar(Long carId) {
        return Optional.of(new Car(carId, "123가4567", "Sonata", 30_000_000L, true));
    }

    @Override
    public void save(Policy policy) {
        PolicyJpaEntity entity = new PolicyJpaEntity(
                policy.getPolicyNumber(),
                policy.getDriverId(),
                policy.getCarId(),
                policy.getStatus().name()
        );
        policyRepository.save(entity);
    }
}
