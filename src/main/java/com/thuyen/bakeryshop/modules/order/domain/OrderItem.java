package com.thuyen.bakeryshop.modules.order.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItem(
        UUID productId,
        String productName,
        int quantity,
        BigDecimal unitPrice
) {
    public BigDecimal subtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
