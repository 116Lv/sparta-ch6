package com.ch6.demo.menu.presentation;

import com.ch6.demo.menu.application.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public MenuResponse getMenus() {
        return MenuResponse.from(menuService.getMenus());
    }
}
