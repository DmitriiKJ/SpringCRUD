package com.example.Shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

@Entity
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderDemo{" +
                "id=" + id +
                ", user=" + user +
                ", orderItems=" + orderItems +
                ", total=" + total +
                '}';
    }
}
