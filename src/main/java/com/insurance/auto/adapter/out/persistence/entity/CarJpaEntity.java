package com.insurance.auto.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "car")
@Getter
@NoArgsConstructor
public class CarJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String carNumber;

    private String modelName;
    private String subModelName;
    private int modelYear;
    private Long price;
    private boolean hasBlackBox;

    public CarJpaEntity(String carNumber, String modelName, String subModelName, int modelYear, Long price, boolean hasBlackBox) {
        this.carNumber = carNumber;
        this.modelName = modelName;
        this.subModelName = subModelName;
        this.modelYear = modelYear;
        this.price = price;
        this.hasBlackBox = hasBlackBox;
    }
}
