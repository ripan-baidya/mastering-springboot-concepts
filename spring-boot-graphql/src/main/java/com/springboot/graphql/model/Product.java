package com.springboot.graphql.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String category;
    private String price;
    private Double stock;

    public Product(String name, String category, String price, Double stock) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }
}
