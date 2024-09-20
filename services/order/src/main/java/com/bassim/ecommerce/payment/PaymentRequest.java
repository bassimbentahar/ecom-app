package com.bassim.ecommerce.payment;

import com.bassim.ecommerce.customer.CustomerResponse;
import com.bassim.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
