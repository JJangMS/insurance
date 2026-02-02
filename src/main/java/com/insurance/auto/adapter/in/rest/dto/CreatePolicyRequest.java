package com.insurance.auto.adapter.in.rest.dto;

public record CreatePolicyRequest(
        Long driverId,
        Long carId
) {}
