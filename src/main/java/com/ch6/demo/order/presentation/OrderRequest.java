package com.ch6.demo.order.presentation;

import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotNull Long userId,
        @NotNull Long menuId
) {
}
