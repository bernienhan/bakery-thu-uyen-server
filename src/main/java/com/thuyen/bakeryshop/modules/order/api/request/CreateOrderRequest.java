package com.thuyen.bakeryshop.modules.order.api.request;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        UUID customerId,
        List<OrderItemRequest> items
) {
    public record OrderItemRequest(UUID productId, int quantity) {
    }
}
