package com.insurance.auto.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.auto.application.port.in.RegisterDriverCarCommand;
import com.insurance.auto.application.port.in.dto.CustomerInquiryInfo;
import com.insurance.auto.application.service.AutoPolicyService;
import com.insurance.auto.domain.model.Car;
import com.insurance.auto.domain.model.Driver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DriverController.class)
class DriverControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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

    @Test
    @DisplayName("신규 운전자와 차량 등록 성공")
    void register_driver_success() throws Exception {
        // Given
        RegisterDriverCarCommand command = new RegisterDriverCarCommand(
                "장민수수",
                LocalDate.of(1995, 5, 5),
                "010-9999-8888",
                "123가4567",
                "모델y",
                "듀얼모터",
                2026
        );

        Driver savedDriver = new Driver(2L, command.name(), command.birthDate(), 0, command.phone());
        Car savedCar = new Car(2L, command.carNumber(), command.carModel(), command.carModelName(), command.carModelYear(), 20_000_000L, true);
        CustomerInquiryInfo info = new CustomerInquiryInfo(savedDriver, savedCar);

        given(autoPolicyService.register(any(RegisterDriverCarCommand.class)))
                .willReturn(info);

        // When & Then
        mockMvc.perform(post("/api/v1/driver/register")
                        .contentType(MediaType.APPLICATION_JSON) // JSON으로 보낸다고 명시
                        .content(objectMapper.writeValueAsString(command))) // 객체를 JSON 문자열로 변환
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("장민수수"))
                .andExpect(jsonPath("$.carNumber").value("123가4567"))
                .andExpect(jsonPath("$.driverId").exists());
    }
}
