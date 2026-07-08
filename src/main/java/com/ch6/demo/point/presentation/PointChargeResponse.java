package com.ch6.demo.point.presentation;

import com.ch6.demo.point.domain.UserPoint;

public record PointChargeResponse(
        Long userId,
        int chargedAmount,
        int balance
) {

    public static PointChargeResponse of(UserPoint userPoint, int chargedAmount) {
        return new PointChargeResponse(
                userPoint.getUserId(),
                chargedAmount,
                userPoint.getBalance()
        );
    }
}
