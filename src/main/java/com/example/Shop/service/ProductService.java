package com.example.Shop.service;

import com.example.Shop.model.Product;
import com.example.Shop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements ProductRepository {
    private static int nextId;
    private List<Product> products = new ArrayList<>();

    public ProductService(){
        this.products.add(new Product(1, "Iphone1", "Apple1", 250000, 1));
        this.products.add(new Product(2, "Iphone2", "Apple2", 500000, 5));
        this.products.add(new Product(3, "Iphone3", "AppleChina", 2100, 100));
        nextId = 4;
    }

    @Override
    public void save(Product product) {
        product.setId(nextId++);
        products.add(product);
    }

    @Override
    public Optional<Product> findById(long id) {
        return products.stream().filter(p -> p.getId() == id).findFirst();
    }

    @Override
    public List<Product> getAll() {
        return products;
    }

    @Override
    public void delete(Product product) {
        products.remove(product);
    }

    @Override
    public void update(long id, Product product) {
        products.set(products.indexOf(findById(id).get()), product);
    }
}
