package com.thuyen.bakeryshop.modules.order.api.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String status,
        List<OrderItemResponse> items,
        BigDecimal totalAmount
) {
}
