package com.bassim.ecommerce.customer;

import org.springframework.validation.annotation.Validated;

@Validated
public record Address(String street, String houseNumber, String zipCode) {
}
