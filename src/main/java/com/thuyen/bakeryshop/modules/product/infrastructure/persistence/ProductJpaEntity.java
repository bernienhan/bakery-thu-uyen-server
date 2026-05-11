package com.thuyen.bakeryshop.modules.product.infrastructure.persistence;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
public class ProductJpaEntity {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String status;

}
