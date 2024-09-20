package com.bassim.ecommerce.kafka;

import com.bassim.ecommerce.customer.CustomerResponse;
import com.bassim.ecommerce.order.PaymentMethod;
import com.bassim.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(

        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
