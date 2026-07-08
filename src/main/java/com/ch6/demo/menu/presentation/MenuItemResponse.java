package com.ch6.demo.menu.presentation;

import com.ch6.demo.menu.domain.Menu;

public record MenuItemResponse(
        Long id,
        String name,
        int price
) {

    public static MenuItemResponse from(Menu menu) {
        return new MenuItemResponse(menu.getId(), menu.getName(), menu.getPrice());
    }
}
