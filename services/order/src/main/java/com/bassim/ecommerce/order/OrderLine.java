package com.bassim.ecommerce.order;


import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table
public class OrderLine {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private double quantity;
}
