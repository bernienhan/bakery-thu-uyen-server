package com.thuyen.bakeryshop.modules.payment.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record Payment(
        UUID id,
        UUID orderId,
        BigDecimal amount,
        PaymentStatus status
) {
}
