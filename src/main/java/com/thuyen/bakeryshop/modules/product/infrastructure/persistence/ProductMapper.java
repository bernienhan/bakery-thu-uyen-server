package com.thuyen.bakeryshop.modules.product.infrastructure.persistence;

import com.thuyen.bakeryshop.modules.product.domain.Product;
import com.thuyen.bakeryshop.modules.product.domain.ProductStatus;

public class ProductMapper {

    public Product toDomain(ProductJpaEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                null,
                ProductStatus.valueOf(entity.getStatus())
        );
    }
}
