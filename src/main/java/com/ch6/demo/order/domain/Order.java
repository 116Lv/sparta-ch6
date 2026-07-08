package com.ch6.demo.order.domain;

import com.ch6.demo.menu.domain.Menu;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private int paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime orderedAt;

    protected Order() {
    }

    public Order(Long userId, Menu menu) {
        this.userId = userId;
        this.menu = menu;
        this.paymentAmount = menu.getPrice();
        this.status = OrderStatus.PAID;
        this.orderedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
