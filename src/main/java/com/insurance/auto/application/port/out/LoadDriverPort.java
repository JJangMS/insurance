package com.insurance.auto.application.port.out;

import com.insurance.auto.domain.model.Driver;
import java.util.Optional;

public interface LoadDriverPort {
    Optional<Driver> loadDriver(Long driverId);
}
