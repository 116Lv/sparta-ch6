package com.ch6.demo.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_outbox")
public class OrderOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private int paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderOutboxStatus status;

    @Column(nullable = false)
    private int retryCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected OrderOutbox() {
    }

    public OrderOutbox(Order order) {
        this.order = order;
        this.userId = order.getUserId();
        this.menuId = order.getMenu().getId();
        this.paymentAmount = order.getPaymentAmount();
        this.status = OrderOutboxStatus.PENDING;
        this.retryCount = 0;
        this.createdAt = LocalDateTime.now();
    }
}
