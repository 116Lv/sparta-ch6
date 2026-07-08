package com.ch6.demo.menu.presentation;

import com.ch6.demo.menu.application.PopularMenuResult;

public record PopularMenuItemResponse(
        Long menuId,
        String name,
        int price,
        long orderCount
) {

    public static PopularMenuItemResponse from(PopularMenuResult popularMenu) {
        return new PopularMenuItemResponse(
                popularMenu.menuId(),
                popularMenu.name(),
                popularMenu.price(),
                popularMenu.orderCount()
        );
    }
}
