package com.ch6.demo.order.presentation;

import com.ch6.demo.order.application.OrderResult;
import com.ch6.demo.order.application.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest request) {
        OrderResult result = orderService.createOrder(request.userId(), request.menuId());
        return OrderResponse.from(result);
    }
}
