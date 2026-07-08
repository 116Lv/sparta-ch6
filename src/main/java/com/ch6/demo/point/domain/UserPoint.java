package com.ch6.demo.point.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_points")
public class UserPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private int balance;

    protected UserPoint() {
    }

    public UserPoint(Long userId) {
        this.userId = userId;
        this.balance = 0;
    }

    public void charge(int amount) {
        this.balance += amount;
    }

    public Long getUserId() {
        return userId;
    }

    public int getBalance() {
        return balance;
    }
}
