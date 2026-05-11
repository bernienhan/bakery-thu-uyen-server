package com.thuyen.bakeryshop.modules.order.application;

import com.thuyen.bakeryshop.modules.order.domain.Order;

import java.math.BigDecimal;

public class OrderPricingService {

    public BigDecimal calculateTotal(Order order) {
        return order.totalAmount();
    }
}
