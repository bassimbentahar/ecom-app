package com.bassim.ecommerce.order;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Mono<Integer>> createOrder(
            @RequestBody @Valid OrderRequest request
    ){
        return ResponseEntity.ok(service.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<Flux<OrderResponse>> findAll(){
          return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/order-id")
    public ResponseEntity<Mono<OrderResponse>> getById(
            @PathVariable ("order-id") Integer id
    ){
        return ResponseEntity.ok(service.findById(id));
    }

}
