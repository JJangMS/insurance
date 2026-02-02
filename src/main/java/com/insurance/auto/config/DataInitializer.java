package com.insurance.auto.config;

import com.insurance.auto.adapter.out.persistence.entity.CarJpaEntity;
import com.insurance.auto.adapter.out.persistence.entity.DriverJpaEntity;
import com.insurance.auto.adapter.out.persistence.entity.PolicyJpaEntity;
import com.insurance.auto.adapter.out.persistence.repository.CarJpaRepository;
import com.insurance.auto.adapter.out.persistence.repository.DriverJpaRepository;
import com.insurance.auto.adapter.out.persistence.repository.PolicyJpaRepository;
import com.insurance.auto.domain.model.PolicyStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DriverJpaRepository driverRepository;
    private final CarJpaRepository carRepository;
    private final PolicyJpaRepository policyRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (driverRepository.count() > 0) {
            return;
        }

        DriverJpaEntity driver = new DriverJpaEntity(
                "장민수",
                LocalDate.of(1993, 2, 12),
                "010-1234-5678",
                0
        );
        DriverJpaEntity savedDriver = driverRepository.save(driver);

        CarJpaEntity car = new CarJpaEntity(
                "234오6789",
                "그랜저",
                "더 뉴 그랜저IG 2.5 가솔린",
                2021,
                19_390_000L,
                true
        );
        CarJpaEntity savedCar = carRepository.save(car);

        PolicyJpaEntity policy = PolicyJpaEntity.builder()
                .id(null)
                .policyNumber("P-123")
                .driverId(savedDriver.getId())
                .carId(savedCar.getId())
                .premium(802_360L)
                .status(PolicyStatus.APPROVED.name())
                .startDate(LocalDate.of(2025, 10, 21))
                .endDate(LocalDate.of(2026, 10, 20))
                .createdAt(LocalDateTime.now())
                .build();

        policyRepository.save(policy);

        log.info("=========================================");
        log.info("JJ::Dummy Insert");
        log.info("=========================================");
    }
}
