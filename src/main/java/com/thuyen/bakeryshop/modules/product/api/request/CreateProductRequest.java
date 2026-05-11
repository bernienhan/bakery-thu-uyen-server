package com.thuyen.bakeryshop.modules.product.api.request;

import java.math.BigDecimal;

public record CreateProductRequest(
        String name,
        String description,
        BigDecimal price,
        String categoryId
) {
}
