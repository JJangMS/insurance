package com.insurance.auto.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.auto.adapter.in.rest.dto.ApproveRequest;
import com.insurance.auto.adapter.in.rest.dto.CreatePolicyRequest;
import com.insurance.auto.application.port.in.RegisterPolicyCommand;
import com.insurance.auto.application.port.in.RegisterPolicyUseCase;
import com.insurance.auto.domain.model.Policy;
import com.insurance.auto.domain.model.PolicyStatus;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PolicyController.class)
class PolicyControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean RegisterPolicyUseCase registerPolicyUseCase;

    @Test
    @DisplayName("임시 정책 생성 API 호출 성공")
    void create_temporary_policy() throws Exception {
        // Given
        CreatePolicyRequest request = new CreatePolicyRequest(1L, 1L);
        Policy mockPolicy = new Policy(1L, 1L); // REVIEWING
        given(registerPolicyUseCase.create(any(RegisterPolicyCommand.class)))
                .willReturn(mockPolicy);

        // When, Then
        mockMvc.perform(post("/api/v1/policy/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REVIEWING"));
    }

    @Test
    @DisplayName("가입 승인 API 호출 성공")
    void approve_policy() throws Exception {
        // Given
        LocalDate startDate = LocalDate.of(2026, 2, 2);
        ApproveRequest request = new ApproveRequest(startDate);
        Policy approved = new Policy(1L, "P-123", 1L, 1L, 800000L, PolicyStatus.APPROVED, startDate, startDate.plusYears(1));
        given(registerPolicyUseCase.approve(1L, startDate))
                .willReturn(approved);

        // When, Then
        mockMvc.perform(post("/api/v1/policy/1/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.startDate").value("2026-02-02"));
    }
}
