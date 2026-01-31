package com.insurance.auto.application.port.out;

import com.insurance.auto.domain.model.Car;
import com.insurance.auto.domain.model.Driver;
import java.util.Optional;

public interface LoadInsuredPort {
    Optional<Driver> loadDriver(Long driverId);
    Optional<Car> loadCar(Long carId);
}
