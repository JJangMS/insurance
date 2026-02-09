package com.insurance.auto.application.port.in;

import com.insurance.auto.application.port.in.dto.CustomerInquiryInfo;

import java.time.LocalDate;

public interface InquiryCustomerQuery {
    CustomerInquiryInfo inquireCustomer(String name, LocalDate birthDate, String phone);
}
