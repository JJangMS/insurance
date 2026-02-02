package com.insurance.auto.adapter.in.rest.dto;

import com.insurance.auto.application.port.in.dto.CustomerInquiryInfo;
import com.insurance.auto.domain.model.Car;
import com.insurance.auto.domain.model.Driver;

public record InquiryResponse(Long driverId,
                              String name,
                              Long carId,
                              String carNumber,
                              String carModel
) {
    public static InquiryResponse from(CustomerInquiryInfo info) {
        Driver driver = info.driver();
        Car car = info.car();
        return new InquiryResponse(
                driver.id(),
                driver.name(),
                car != null ? car.id() : null,
                car != null ? car.carNumber() : null,
                car != null ? car.modelName() : null
        );
    }
}
