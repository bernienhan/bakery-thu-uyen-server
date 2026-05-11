package com.thuyen.bakeryshop.modules.product.infrastructure.persistence;

import com.thuyen.bakeryshop.modules.product.domain.Product;
import com.thuyen.bakeryshop.modules.product.domain.ProductRepository;

import java.util.Optional;
import java.util.UUID;

public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public Optional<Product> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Product save(Product product) {
        return product;
    }
}
