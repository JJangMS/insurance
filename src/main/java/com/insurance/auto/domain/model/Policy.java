package com.insurance.auto.domain.model;

import lombok.Getter;
import java.util.UUID;

@Getter
public class Policy {
    private Long id;
    private final String policyNumber;
    private final Long driverId;
    private final Long carId;

    private PolicyStatus status;

    public Policy(Long driverId, Long carId) {
        this.policyNumber = UUID.randomUUID().toString();
        this.driverId = driverId;
        this.carId = carId;
        this.status = PolicyStatus.REVIEWING;
    }

    // 만 21세 미만 or 사고 3회 이상 REJECTED
    public void underwrite(Driver driver) {
        if (driver.getAge() < 21 || driver.accidentHistoryCount() >= 3) {
            this.status = PolicyStatus.REJECTED;
        } else {
            this.status = PolicyStatus.APPROVED;
        }
    }
}
