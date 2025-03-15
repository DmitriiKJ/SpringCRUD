package com.example.Shop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Product product;

    @PositiveOrZero(message = "Stock can't be negative")
    @Column(nullable = false, length = Integer.MAX_VALUE)
    private int quantity;

    @Column(nullable = false, precision = 8, scale = 2)
    @Positive(message = "Price can't be negative or zero")
    private BigDecimal price;

    @ManyToOne
    @JsonBackReference
    private OrderDemo order;
}
