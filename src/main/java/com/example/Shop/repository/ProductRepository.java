package com.example.Shop.repository;

import com.example.Shop.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();

    @Query(value = "SELECT * FROM Product WHERE name = :name", nativeQuery = true)

    List<Product> findByNameContaining(@Param("name")String name);

    Optional<Product> findById(Long id);

    void deleteById(Long id);

    Product save(Product product);
}
