package com.insurance.auto.adapter.in.rest;

import com.insurance.auto.adapter.in.rest.dto.ApproveRequest;
import com.insurance.auto.adapter.in.rest.dto.CreatePolicyRequest;
import com.insurance.auto.adapter.in.rest.dto.PolicyResponse;
import com.insurance.auto.application.port.in.RegisterPolicyCommand;
import com.insurance.auto.application.port.in.RegisterPolicyUseCase;
import com.insurance.auto.domain.model.Policy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/policy")
public class PolicyController {

    private final RegisterPolicyUseCase registerPolicyUseCase;

    @PostMapping("/create")
    public ResponseEntity<PolicyResponse> createInsurancePolicy(@RequestBody CreatePolicyRequest request) {
        log.info("JJ::createInsurancePolicy:: Driver: {}, Car: {}", request.driverId(), request.carId());

        RegisterPolicyCommand command = new RegisterPolicyCommand(request.driverId(), request.carId());
        Policy policy = registerPolicyUseCase.create(command);

        return ResponseEntity.ok(PolicyResponse.from(policy));
    }

    @PostMapping("/{policyId}/approve")
    public ResponseEntity<PolicyResponse> approveInsurance(@PathVariable Long policyId, @RequestBody ApproveRequest request) {
        log.info("JJ::approveInsurance:: PolicyID: {}, StartDate: {}", policyId, request.startDate());

        Policy policy = registerPolicyUseCase.approve(policyId, request.startDate());

        return ResponseEntity.ok(PolicyResponse.from(policy));
    }
}
