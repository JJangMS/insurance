package com.insurance.auto.application.port.in;

import com.insurance.auto.application.port.in.dto.CustomerInquiryInfo;

public interface RegisterDriverCarUseCase {
    CustomerInquiryInfo register(RegisterDriverCarCommand command);
}
