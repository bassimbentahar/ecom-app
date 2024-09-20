package com.bassim.ecommerce.product;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Integer> {
    Flux<Product> findAllByIdInOrderById(List<Integer> productIds);

}
