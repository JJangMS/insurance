package com.insurance.auto.adapter.in.rest;

import com.insurance.auto.application.port.in.dto.CustomerInquiryInfo;
import com.insurance.auto.application.service.AutoPolicyService;
import com.insurance.auto.domain.model.Car;
import com.insurance.auto.domain.model.Driver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DriverController.class)
class DriverControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AutoPolicyService autoPolicyService;

    @Test
    @DisplayName("기존 고객 조회 성공")
    void inquire_driver_success() throws Exception {
        // Given
        String name = "장민수";
        String birthDate = "1993-02-12";
        String phone = "010-1234-5678";
        LocalDate birthDateParsed = LocalDate.of(1993, 2, 12);

        Driver mockDriver = new Driver(1L, name, birthDateParsed, 0, phone);
        Car mockCar = new Car(1L, "234오6789", "그랜저", "더 뉴 그랜저IG 2.5 가솔린", 2021, 19_390_000L, true);
        CustomerInquiryInfo mockInfo = new CustomerInquiryInfo(mockDriver, mockCar);

        given(autoPolicyService.inquireCustomer(name, birthDateParsed, phone))
                .willReturn(mockInfo);

        // When, Then
        mockMvc.perform(get("/api/v1/driver/inquiry")
                        .param("name", name)
                        .param("birthDate", birthDate)
                        .param("phone", phone))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driverId").value(1L))
                .andExpect(jsonPath("$.name").value("장민수"))
                .andExpect(jsonPath("$.carModel").value("그랜저"));
    }

    @Test
    @DisplayName("고객 정보가 없으면 204")
    void inquire_driver_not_found() throws Exception {
        // Given
        String name = "없는사람";
        String birthDate = "2000-01-01";
        String phone = "010-0000-0000";
        LocalDate birthDateParsed = LocalDate.of(2000, 1, 1);

        // null 리턴
        given(autoPolicyService.inquireCustomer(name, birthDateParsed, phone))
                .willReturn(null);

        // When, Then
        mockMvc.perform(get("/api/v1/driver/inquiry")
                        .param("name", name)
                        .param("birthDate", birthDate)
                        .param("phone", phone))
                .andExpect(status().isNoContent());
    }
}
