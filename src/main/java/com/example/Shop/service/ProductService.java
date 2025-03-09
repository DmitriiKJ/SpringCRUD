package com.example.Shop.service;

import com.example.Shop.model.Product;
import com.example.Shop.repository.OrderItemRepository;
import com.example.Shop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(long id) {
        return productRepository.getById(id);
    }

    public List<Product> searchProductByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    public Product addProduct(Product product){
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProductById(Long productId) {
        System.out.println("1");
        // Удаляем все связанные записи в order_item

        orderItemRepository.deleteOrderItemRelations(productId);

        orderItemRepository.deleteByProductId(productId);
        System.out.println("2");

        // Удаляем сам продукт
        productRepository.deleteById(productId);
        System.out.println("3");

    }

    public Product updateProduct(Product product){
        return productRepository.save(product);
    }

}
