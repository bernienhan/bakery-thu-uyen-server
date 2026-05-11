package com.thuyen.bakeryshop.modules.order.infrastructure.persistence;

import java.util.UUID;

public class OrderJpaEntity {

    private UUID id;
    private UUID customerId;
    private String status;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
