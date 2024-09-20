package com.bassim.ecommerce.product;

import jakarta.persistence.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table("product")  // Maps this entity to the "product" table in the database
public class Product {

    @Id
    @GeneratedValue
    private Integer id; // Primary key

    private String name;

    private String description;

    private double availableQuantity;

    private BigDecimal price;

    @Column("category_id") // Specifies the column name for this field in the table
    private Integer categoryId;
}
