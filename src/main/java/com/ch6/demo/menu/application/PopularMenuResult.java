package com.ch6.demo.menu.application;

public record PopularMenuResult(
        Long menuId,
        String name,
        int price,
        long orderCount
) {
}
