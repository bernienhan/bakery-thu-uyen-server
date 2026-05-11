package com.thuyen.bakeryshop.modules.product.api.request;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,
        String description,
        BigDecimal price,
        String status
) {
}
