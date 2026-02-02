package com.insurance.auto.application.port.in.dto;

import com.insurance.auto.domain.model.Car;
import com.insurance.auto.domain.model.Driver;

public record CustomerInquiryInfo(
        Driver driver,
        Car car
) {}
