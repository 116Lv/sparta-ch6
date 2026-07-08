package com.ch6.demo.order.repository;

import com.ch6.demo.order.domain.OrderOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, Long> {
}
