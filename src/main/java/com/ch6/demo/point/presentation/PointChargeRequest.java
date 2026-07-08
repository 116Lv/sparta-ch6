package com.ch6.demo.point.presentation;

import jakarta.validation.constraints.Positive;

public record PointChargeRequest(
        @Positive int amount
) {
}
