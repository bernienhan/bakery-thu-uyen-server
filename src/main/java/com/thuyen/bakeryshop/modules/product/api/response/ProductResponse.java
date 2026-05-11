package com.thuyen.bakeryshop.modules.product.api.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        String status
) {
}
