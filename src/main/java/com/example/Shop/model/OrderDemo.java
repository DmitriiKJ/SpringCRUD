package com.example.Shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
@Table(name = "`Order`")
public class OrderDemo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @OneToMany
    private List<OrderItem> orderItems;

    @Column(nullable = false, precision = 10, scale = 2)
    @Positive(message = "Price can't be negative or zero")
    private BigDecimal total;
}
