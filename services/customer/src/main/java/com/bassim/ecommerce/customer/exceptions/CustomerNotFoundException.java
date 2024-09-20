package com.bassim.ecommerce.customer.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    private final String msg;

    public CustomerNotFoundException(String s, String msg) {
        this.msg = msg;
    }
}
