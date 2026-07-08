package com.ch6.demo.order.application;

import com.ch6.demo.order.domain.Order;

public record OrderResult(
        Order order,
        int remainingPoint
) {
}
