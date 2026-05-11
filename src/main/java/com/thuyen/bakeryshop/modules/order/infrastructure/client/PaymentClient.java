package com.thuyen.bakeryshop.modules.order.infrastructure.client;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentClient {

    public boolean requestPayment(UUID orderId, BigDecimal amount) {
        return true;
    }
}
