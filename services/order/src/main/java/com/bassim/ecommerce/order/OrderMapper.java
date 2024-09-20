package com.bassim.ecommerce.order;

import org.springframework.stereotype.Service;

@Service
public class OrderMapper {
    public CustomerOrder toOrder(OrderRequest request) {
        return CustomerOrder.builder()
                .customerId(request.customerId())
                .reference(request.reference())
                .totalAmount(request.amount())
                .paymentMethod(request.paymentMethod())
                .build();
    }

    public OrderResponse fromOrder(CustomerOrder customerOrder) {
        return new OrderResponse(
                customerOrder.getId(),
                customerOrder.getReference(),
                customerOrder.getTotalAmount(),
                customerOrder.getPaymentMethod(),
                customerOrder.getCustomerId()
        );
    }
}
