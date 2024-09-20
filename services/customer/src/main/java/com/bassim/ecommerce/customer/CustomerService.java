package com.bassim.ecommerce.customer;

import com.bassim.ecommerce.customer.exceptions.CustomerNotFoundException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;


@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerService(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Mono<String> createCustomer(CustomerRequest request) {
        return  repository.save(mapper.toCustomer(request))
                .map(Customer::getId);
    }

    public Mono<Customer> updateCustomer(CustomerRequest request) {
        var customer = repository.findById(request.id())
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(
                        "Cannot update customer: no customer found with the provided ID {%s}", request.id() ))
                ).block();//

        mergeCustomer(customer, request);
        return repository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstName())){
            customer.setFirstname(request.firstName());
        }
        if (StringUtils.isNotBlank(request.lastName())){
            customer.setLastname(request.lastName());
        }
        if (StringUtils.isNotBlank(request.email())){
            customer.setEmail(request.email());
        }
        if (request.address() != null){
            customer.setAddress(request.address());
        }
    }

    public Mono<List<CustomerResponse>> findAllCustomers() {
        return repository.findAll()
                .map(mapper::toCustomerResponse)
                .collect(Collectors.toList());
    }

    public Mono<CustomerResponse> findCustomerById(String id) {
        return repository.findById(id)
                .map(mapper::toCustomerResponse)
                .switchIfEmpty(Mono.error(
                        new CustomerNotFoundException(
                                "Cannot update customer: no customer found with the provided ID {%s}", format("Cannot update customer: no customer found with the provided ID {%s}", id))
                ));

    }

    public Mono<Boolean> existsById(String id) {
        return repository.findById(id).hasElement();
    }

    public void deleteCustomer(String id) {
        repository.deleteById(id).subscribe();
    }
}
