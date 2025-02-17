package com.example.Shop.controller;

import com.example.Shop.model.Product;
import com.example.Shop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {
//    private final ProductService productService;
//
//    public HomeController(ProductService productService){
//        this.productService = productService;
//    }
//
//    @GetMapping
//    public String index(){
//        return "index";
//    }
//
//    // Read
//    @GetMapping("/read")
//    public String read(Model model){
//        model.addAttribute("products", productService.getAll());
//        return "crud/read";
//    }
//
//    // Create
//    @GetMapping("/create")
//    public String create(Model model){
//        return "crud/save";
//    }
//
//    @PostMapping("/create")
//    public String create(@ModelAttribute Product product){
//        productService.save(product);
//        return "redirect:/read";
//    }
//
//    // Delete
//    @GetMapping("/delete")
//    public String delete(@RequestParam int id, Model model){
//        Optional<Product> product = productService.findById(id);
//        model.addAttribute("product", product);
//        return "crud/delete";
//    }
//
//    @PostMapping("/delete")
//    public String delete(@RequestParam int id){
//        Optional<Product> product = productService.findById(id);
//        if(product.isPresent()) productService.delete(product.get());
//        return "redirect:/read";
//    }
//
//    // Update
//    @GetMapping("/update")
//    public String update(@RequestParam int id, Model model){
//        model.addAttribute("product", productService.findById(id));
//        return "crud/update";
//    }
//
//    @PostMapping("/update")
//    public String update(@ModelAttribute Product product){
//        productService.update(product.getId(), product);
//        return "redirect:/read";
//    }
}
