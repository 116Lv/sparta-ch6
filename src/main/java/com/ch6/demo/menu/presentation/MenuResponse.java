package com.ch6.demo.menu.presentation;

import com.ch6.demo.menu.domain.Menu;

import java.util.List;

public record MenuResponse(List<MenuItemResponse> menus) {

    public static MenuResponse from(List<Menu> menus) {
        return new MenuResponse(menus.stream()
                .map(MenuItemResponse::from)
                .toList());
    }
}
