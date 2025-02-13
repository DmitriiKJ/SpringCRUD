package com.example.Shop.repository;

import com.example.Shop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void save(Product product);
    Optional<Product> findById(long id);
    List<Product> getAll();
    void delete(Product product);
    void update(long id, Product product);
}
