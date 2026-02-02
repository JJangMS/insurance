package com.insurance.auto.domain.model;

import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Policy {
    private Long id;
    private final String policyNumber;
    private final Long driverId;
    private final Long carId;
    private Long premium;
    private PolicyStatus status;
    private LocalDate startDate;
    private LocalDate endDate;

    public Policy(Long driverId, Long carId) {
        this.policyNumber = UUID.randomUUID().toString();
        this.driverId = driverId;
        this.carId = carId;
        this.status = PolicyStatus.REVIEWING;
    }

    public Policy(Long id, String policyNumber, Long driverId, Long carId,
                  Long premium, PolicyStatus status, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.policyNumber = policyNumber;
        this.driverId = driverId;
        this.carId = carId;
        this.premium = premium;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // 만 21세 미만 or 사고 3회 이상 REJECTED
    public void underwrite(Driver driver) {
        if (driver.getAge() < 21 || driver.accidentHistoryCount() >= 3) {
            this.status = PolicyStatus.REJECTED;
        } else {
            this.status = PolicyStatus.APPROVED;
        }
    }

    public void approve(Driver driver, LocalDate requestedStartDate, Long calculatedPremium) {
        underwrite(driver);

        if (this.status == PolicyStatus.APPROVED) {
            this.startDate = requestedStartDate;
            this.endDate = requestedStartDate.plusYears(1).minusDays(1);
            this.premium = calculatedPremium;
        }
    }
}
