package com.thuyen.bakeryshop.modules.product.domain;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Optional<Product> findById(UUID id);

    Product save(Product product);
}
