package com.insurance.auto.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "driver")
@Getter
@NoArgsConstructor
public class DriverJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate birthDate;
    private String phone;
    private int accidentHistoryCount;

    public DriverJpaEntity(String name, LocalDate birthDate, String phone, int accidentHistoryCount) {
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.accidentHistoryCount = accidentHistoryCount;
    }
}
