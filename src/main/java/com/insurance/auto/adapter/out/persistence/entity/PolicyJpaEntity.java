package com.insurance.auto.adapter.out.persistence.entity;

import com.insurance.auto.domain.model.Policy;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "policy")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String policyNumber;

    private Long driverId;
    private Long carId;
    private Long premium;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;

    public static PolicyJpaEntity from(Policy policy) {
        return PolicyJpaEntity.builder()
                .id(null)
                .policyNumber(policy.getPolicyNumber())
                .driverId(policy.getDriverId())
                .carId(policy.getCarId())
                .premium(policy.getPremium())
                .status(policy.getStatus().name())
                .startDate(policy.getStartDate())
                .endDate(policy.getEndDate())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void updatePolicy(Policy policy) {
        this.policyNumber = policy.getPolicyNumber();
        this.driverId = policy.getDriverId();
        this.carId = policy.getCarId();
        this.premium = policy.getPremium();
        this.status = policy.getStatus().name();
        this.startDate = policy.getStartDate();
        this.endDate = policy.getEndDate();
    }
}
