package com.insurance.auto.adapter.in.rest;

import com.insurance.auto.application.port.in.RegisterPolicyCommand;
import com.insurance.auto.application.port.in.RegisterPolicyUseCase;
import com.insurance.auto.domain.model.Policy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PolicyController {

    private final RegisterPolicyUseCase registerPolicyUseCase;

    @PostMapping("/api/v1/policies")
    public ResponseEntity<PolicyResponse> registerPolicy(@RequestBody RegisterRequest request) {
        RegisterPolicyCommand command = new RegisterPolicyCommand(request.driverId(), request.carId());

        Policy policy = registerPolicyUseCase.register(command);

        return ResponseEntity.ok(new PolicyResponse(
                policy.getPolicyNumber(),
                policy.getStatus().name()
        ));
    }

    public record RegisterRequest(Long driverId, Long carId) {}
    public record PolicyResponse(String policyNumber, String status) {}
}
