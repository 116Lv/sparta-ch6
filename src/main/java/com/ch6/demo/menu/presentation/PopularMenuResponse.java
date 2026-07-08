package com.ch6.demo.menu.presentation;

import com.ch6.demo.menu.application.PopularMenuResult;

import java.util.List;

public record PopularMenuResponse(List<PopularMenuItemResponse> popularMenus) {

    public static PopularMenuResponse from(List<PopularMenuResult> popularMenus) {
        return new PopularMenuResponse(popularMenus.stream()
                .map(PopularMenuItemResponse::from)
                .toList());
    }
}
