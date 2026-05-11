package com.thuyen.bakeryshop.modules.order.infrastructure.client;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductClient {

    public ProductSnapshot getProduct(UUID productId) {
        return new ProductSnapshot(productId, "Demo product", BigDecimal.ZERO);
    }

    public record ProductSnapshot(UUID id, String name, BigDecimal price) {
    }
}
