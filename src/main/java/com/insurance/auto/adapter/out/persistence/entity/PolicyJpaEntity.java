package com.insurance.auto.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "policy")
@Getter
@NoArgsConstructor
public class PolicyJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String policyNumber;

    private Long driverId;
    private Long carId;
    private String status;
    private LocalDateTime createdAt;

    public PolicyJpaEntity(String policyNumber, Long driverId, Long carId, String status) {
        this.policyNumber = policyNumber;
        this.driverId = driverId;
        this.carId = carId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }
}
