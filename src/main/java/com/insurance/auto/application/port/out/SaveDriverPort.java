package com.insurance.auto.application.port.out;

import com.insurance.auto.domain.model.Driver;

public interface SaveDriverPort {
    Driver save(Driver driver);
}
