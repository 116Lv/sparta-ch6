package com.ch6.demo.point.application;

import com.ch6.demo.point.domain.UserPoint;
import com.ch6.demo.point.repository.UserPointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    private final UserPointRepository userPointRepository;

    public PointService(UserPointRepository userPointRepository) {
        this.userPointRepository = userPointRepository;
    }

    @Transactional
    public UserPoint charge(Long userId, int amount) {
        UserPoint userPoint = userPointRepository.findByUserId(userId)
                .orElseGet(() -> new UserPoint(userId));
        userPoint.charge(amount);
        return userPointRepository.save(userPoint);
    }
}
