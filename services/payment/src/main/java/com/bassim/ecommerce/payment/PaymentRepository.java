package com.bassim.ecommerce.payment;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends R2dbcRepository<Payment, Integer> {
}
