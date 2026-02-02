package com.insurance.auto.application.port.out;

import com.insurance.auto.domain.model.Car;
import java.util.Optional;

public interface LoadCarPort {
    Optional<Car> loadCar(Long carId);
}
