package com.insurance.auto.application.port.out;

import com.insurance.auto.domain.model.Driver;

import java.time.LocalDate;
import java.util.Optional;

public interface LoadDriverPort {
    Optional<Driver> loadDriver(Long driverId);
    Optional<Driver> loadDriver(String name, LocalDate birthDate, String phone);
}
