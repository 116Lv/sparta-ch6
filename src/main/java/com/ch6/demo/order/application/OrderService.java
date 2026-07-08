package com.ch6.demo.order.application;

import com.ch6.demo.menu.domain.Menu;
import com.ch6.demo.menu.repository.MenuRepository;
import com.ch6.demo.order.domain.Order;
import com.ch6.demo.order.domain.OrderOutbox;
import com.ch6.demo.order.repository.OrderOutboxRepository;
import com.ch6.demo.order.repository.OrderRepository;
import com.ch6.demo.point.domain.UserPoint;
import com.ch6.demo.point.repository.UserPointRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final UserPointRepository userPointRepository;
    private final OrderRepository orderRepository;
    private final OrderOutboxRepository orderOutboxRepository;

    public OrderService(
            MenuRepository menuRepository,
            UserPointRepository userPointRepository,
            OrderRepository orderRepository,
            OrderOutboxRepository orderOutboxRepository
    ) {
        this.menuRepository = menuRepository;
        this.userPointRepository = userPointRepository;
        this.orderRepository = orderRepository;
        this.orderOutboxRepository = orderOutboxRepository;
    }

    @Transactional
    public OrderResult createOrder(Long userId, Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .filter(Menu::isActive)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found."));

        UserPoint userPoint = userPointRepository.findWithLockByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User point not found."));

        try {
            userPoint.pay(menu.getPrice());
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        userPointRepository.saveAndFlush(userPoint);

        Order order = orderRepository.save(new Order(userId, menu));
        orderOutboxRepository.save(new OrderOutbox(order));

        return new OrderResult(order, userPoint.getBalance());
    }
}
