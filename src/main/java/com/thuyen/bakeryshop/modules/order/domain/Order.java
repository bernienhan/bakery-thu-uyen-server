package com.thuyen.bakeryshop.modules.order.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record Order(
        UUID id,
        UUID customerId,
        List<OrderItem> items,
        OrderStatus status
) {
    public BigDecimal totalAmount() {
        return items.stream()
                .map(OrderItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
