package com.example.Shop.controller;

import com.example.Shop.model.Product;
import com.example.Shop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public String index(){
        return "index";
    }

    // Read
    @GetMapping("/read")
    public String read(Model model){
        model.addAttribute("products", productService.getAllProducts());
        return "crud/read";
    }

    // Create
    @GetMapping("/create")
    public String create(Model model){
        return "crud/save";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Product product){
        productService.addProduct(product);
        return "redirect:/products/read";
    }

    // Delete
    @GetMapping("/delete")
    public String delete(@RequestParam int id, Model model){
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "crud/delete";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int id){
        Product product = productService.getProductById(id);
        productService.deleteProductById(product.getId());
        return "redirect:/products/read";
    }

    // Update
    @GetMapping("/update")
    public String update(@RequestParam int id, Model model){
        model.addAttribute("product", productService.getProductById(id));
        return "crud/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Product product){
        productService.updateProduct(product);
        return "redirect:/products/read";
    }
}
