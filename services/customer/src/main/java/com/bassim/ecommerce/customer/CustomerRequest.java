package com.bassim.ecommerce.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        String id,
        @NotNull(message = "customer first name is required")
        String firstName,
        @NotNull(message = "customer last name is required")
        String lastName,
        @NotNull(message = "customer email name is required")
        @Email(message = "customer email is not a valid email address")
        String email,
        Address address
) {
}
