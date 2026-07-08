package com.ch6.demo.order.presentation;

import com.ch6.demo.order.application.OrderResult;
import com.ch6.demo.order.domain.Order;

public record OrderResponse(
        Long orderId,
        Long userId,
        Long menuId,
        int paymentAmount,
        int remainingPoint,
        String status
) {

    public static OrderResponse from(OrderResult result) {
        Order order = result.order();
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getMenu().getId(),
                order.getPaymentAmount(),
                result.remainingPoint(),
                order.getStatus().name()
        );
    }
}
