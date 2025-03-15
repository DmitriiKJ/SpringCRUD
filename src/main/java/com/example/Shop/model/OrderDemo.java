package com.example.Shop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
public class OrderDemo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonBackReference
    @ManyToOne
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<OrderItem> orderItems;

    @Column(nullable = false, precision = 10, scale = 2)
    @PositiveOrZero
    private BigDecimal total;
}
