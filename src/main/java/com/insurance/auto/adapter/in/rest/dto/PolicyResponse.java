package com.insurance.auto.adapter.in.rest.dto;

import com.insurance.auto.domain.model.Policy;
import java.time.LocalDate;

public record PolicyResponse(
        Long id,
        String policyNumber,
        String status,
        LocalDate startDate,
        LocalDate endDate,
        Long premium
) {
    public static PolicyResponse from(Policy policy) {
        return new PolicyResponse(
                policy.getId(),
                policy.getPolicyNumber(),
                policy.getStatus().name(),
                policy.getStartDate(),
                policy.getEndDate(),
                policy.getPremium()
        );
    }
}
