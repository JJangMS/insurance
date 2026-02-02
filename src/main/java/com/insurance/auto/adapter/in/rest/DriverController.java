package com.insurance.auto.adapter.in.rest;

import com.insurance.auto.adapter.in.rest.dto.InquiryResponse;
import com.insurance.auto.application.port.in.RegisterDriverCarCommand;
import com.insurance.auto.application.port.in.dto.CustomerInquiryInfo;
import com.insurance.auto.application.service.AutoPolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/driver")
public class DriverController {
    private final AutoPolicyService autoPolicyService;

    @GetMapping("/inquiry")
    public ResponseEntity<InquiryResponse> inquireDriver(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
            @RequestParam String phone
    ) {
        log.info("JJ::inquireDriver:: {}, {}, {}", name, birthDate, phone);

        CustomerInquiryInfo info = autoPolicyService.inquireCustomer(name, birthDate, phone);

        if (info == null) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(InquiryResponse.from(info));
    }

    @PostMapping("/register")
    public ResponseEntity<InquiryResponse> registerDriverAndCar(@RequestBody RegisterDriverCarCommand command) {
        log.info("JJ::registerDriver:: {}, {}", command.name(), command.carNumber());
        CustomerInquiryInfo info = autoPolicyService.register(command);

        return ResponseEntity.ok(InquiryResponse.from(info));
    }
}
