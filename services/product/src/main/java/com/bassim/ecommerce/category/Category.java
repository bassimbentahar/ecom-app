package com.bassim.ecommerce.category;

import jakarta.persistence.Entity;
import org.springframework.data.annotation.Id;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "category")
public class Category {
    @Id
    private Integer id;

    private String name;
    private String description;
}

// La relation OneToMany a été supprimée car la gestion est manuelle
// @OneToMany(mappedBy = "category", cascade = CascadeType.RE
