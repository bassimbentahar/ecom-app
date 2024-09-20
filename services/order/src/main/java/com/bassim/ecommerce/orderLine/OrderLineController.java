package com.bassim.ecommerce.orderLine;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/api/v1/order-lines")
public class OrderLineController {

    private final OrderLineService service;


    public OrderLineController(OrderLineService service) {
        this.service = service;
    }

    @GetMapping("/order/{order-id}")
    public ResponseEntity<Flux<OrderLineResponse>> findByOrderId(
            @PathVariable("order-id") Integer orderId
    ){
        return ResponseEntity.ok(service.findAllByOrderId(orderId));
    }
}
