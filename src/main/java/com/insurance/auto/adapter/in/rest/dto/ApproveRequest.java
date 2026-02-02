package com.insurance.auto.adapter.in.rest.dto;

import java.time.LocalDate;

public record ApproveRequest(
        LocalDate startDate
) {}
