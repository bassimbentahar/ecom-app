package com.bassim.ecommerce.orderLine;

import com.bassim.ecommerce.order.OrderLine;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderLineRepository extends R2dbcRepository<OrderLine, Integer> {

    Flux<OrderLine> findAllByOrderId(Integer orderId);
}
