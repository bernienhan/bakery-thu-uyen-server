package com.thuyen.bakeryshop.modules.order.domain;

public class OrderDomainService {

    public boolean canCancel(Order order) {
        return order.status() == OrderStatus.CREATED || order.status() == OrderStatus.CONFIRMED;
    }
}
