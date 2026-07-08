package com.ch6.demo.point.presentation;

import com.ch6.demo.point.application.PointService;
import com.ch6.demo.point.domain.UserPoint;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/{userId}/points")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @PostMapping("/charge")
    public PointChargeResponse charge(
            @PathVariable Long userId,
            @Valid @RequestBody PointChargeRequest request
    ) {
        UserPoint userPoint = pointService.charge(userId, request.amount());
        return PointChargeResponse.of(userPoint, request.amount());
    }
}
