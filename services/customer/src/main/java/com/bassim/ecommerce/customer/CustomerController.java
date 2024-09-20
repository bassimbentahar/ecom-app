package com.bassim.ecommerce.customer;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    private final CustomerService service;

    @PostMapping
    public Mono<ResponseEntity<String>> createCustomer(
            @RequestBody @Valid CustomerRequest request
    ){
        return service.createCustomer(request)
                .map(ResponseEntity::ok);
    }

    @PutMapping
    public Mono<ResponseEntity.BodyBuilder> updateCustomer(
            @RequestBody @Valid CustomerRequest request
    ){
        return service.updateCustomer(request)
                .map(cust->ResponseEntity.accepted());
    }

    @GetMapping
    public Mono<ResponseEntity<List<CustomerResponse>>> findAll(){
        return service.findAllCustomers()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{customer-id}")
    public Mono<ResponseEntity<CustomerResponse>> findCustomerById(@PathVariable("customer-id") String id) {
        return service.findCustomerById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @GetMapping("/exists/{customer-id}")
    public Mono<ResponseEntity<Boolean>> existById(@PathVariable("customer-id") String id){
        return service.existsById(id)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customer-id") String id){
        service.deleteCustomer(id);
        return ResponseEntity.accepted().build();
    }
}
