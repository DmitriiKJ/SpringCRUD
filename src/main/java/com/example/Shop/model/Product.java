package com.example.Shop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 2, max = 255, message = "Name min: 2, max: 255")
    @NotBlank(message = "Name can't be null")
    private String name;

    @Size(min = 0, max = 1024, message = "Description min: 0, max: 1024")
    private String description;

    @Column(nullable = false, precision = 8, scale = 2)
    @Positive(message = "Price can't be negative or zero")
    private BigDecimal price;

    @PositiveOrZero(message = "Stock can't be negative")
    @Column(nullable = false, length = Integer.MAX_VALUE)
    private int stock;

    public Product(long id, String name, String description, BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public Product() {}
}
