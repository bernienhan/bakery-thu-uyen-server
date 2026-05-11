package com.thuyen.bakeryshop.modules.order.infrastructure.persistence;

import com.thuyen.bakeryshop.modules.order.domain.Order;
import com.thuyen.bakeryshop.modules.order.domain.OrderRepository;

import java.util.Optional;
import java.util.UUID;

public class OrderRepositoryImpl implements OrderRepository {

    @Override
    public Optional<Order> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Order save(Order order) {
        return order;
    }
}
