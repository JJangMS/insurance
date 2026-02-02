package com.insurance.auto.adapter.out.persistence.repository;

import com.insurance.auto.adapter.out.persistence.entity.DriverJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DriverJpaRepository extends JpaRepository<DriverJpaEntity, Long> {
    Optional<DriverJpaEntity> findByNameAndBirthDateAndPhone(String name, LocalDate birthDate, String phone);
}
