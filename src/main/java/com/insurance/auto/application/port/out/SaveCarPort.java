package com.insurance.auto.application.port.out;

import com.insurance.auto.domain.model.Car;

public interface SaveCarPort {
    Car save(Car car);
}
