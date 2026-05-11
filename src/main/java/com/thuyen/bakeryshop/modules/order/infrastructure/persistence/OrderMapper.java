package com.thuyen.bakeryshop.modules.order.infrastructure.persistence;

import com.thuyen.bakeryshop.modules.order.domain.Order;
import com.thuyen.bakeryshop.modules.order.domain.OrderStatus;

import java.util.List;

public class OrderMapper {

    public Order toDomain(OrderJpaEntity entity) {
        return new Order(entity.getId(), entity.getCustomerId(), List.of(), OrderStatus.valueOf(entity.getStatus()));
    }
}
