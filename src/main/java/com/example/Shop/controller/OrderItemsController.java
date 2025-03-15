package com.example.Shop.controller;

import com.example.Shop.model.OrderDemo;
import com.example.Shop.model.OrderItem;
import com.example.Shop.repository.OrderDemoRepository;
import com.example.Shop.repository.OrderItemRepository;
import com.example.Shop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/orders/items")
public class OrderItemsController {
    private final OrderItemRepository orderItemRepository;
    private final OrderDemoRepository orderDemoRepository;
    private final ProductService productService;

    public OrderItemsController(OrderItemRepository orderItemRepository, ProductService productService, OrderDemoRepository orderDemoRepository){
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
        this.orderDemoRepository = orderDemoRepository;
    }

    // Read
    @GetMapping("/read")
    public String read(Model model){
        model.addAttribute("items", orderItemRepository.findAll());
        return "order/item/read";
    }

    // Create
    @GetMapping("/create")
    public String create(@RequestParam long ordId, Model model){
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("ordId", ordId);
        return "order/item/save";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute OrderItem orderItem){
        orderItem.setProduct(productService.getProductById(orderItem.getProduct().getId()));
        orderItem.setPrice(orderItem.getProduct().getPrice());

        orderItem.setOrder(orderDemoRepository.findById(orderItem.getOrder().getId()).get());
        orderItem.getOrder().setTotal(orderItem.getOrder().getTotal().add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))));

        orderItemRepository.save(orderItem);
        return "redirect:/orders/read";
    }

    // Delete
    @GetMapping("/delete")
    public String delete(@RequestParam long id, Model model){
        OrderItem orderItem = orderItemRepository.findById(id).get();
        model.addAttribute("item", orderItem);
        return "order/item/delete";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam long id){
        Optional<OrderItem> orderItem = orderItemRepository.findById(id);
        OrderDemo order;
        if (orderItem.isPresent())
        {
            order = orderItem.get().getOrder();
            orderItemRepository.deleteById(id);
            order.setTotal(calcTotal(order));
            orderDemoRepository.save(order);
        }
        return "redirect:/orders/read";
    }

    // Update
    @GetMapping("/update")
    public String update(@RequestParam long id, Model model){
        Optional<OrderItem> orderItem = orderItemRepository.findById(id);
        if (orderItem.isEmpty())
        {
            return "redirect:/orders/read";
        }
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("item", orderItem.get());
        return "order/item/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute OrderItem orderItem){
        orderItem.setProduct(productService.getProductById(orderItem.getProduct().getId()));
        orderItem.setPrice(orderItem.getProduct().getPrice());
        orderItem.setOrder(orderDemoRepository.findById(orderItem.getOrder().getId()).get());
        orderItemRepository.save(orderItem);

        orderItem.getOrder().setTotal(calcTotal(orderItem.getOrder()));
        orderItemRepository.save(orderItem);

        return "redirect:/orders/read";
    }

    public BigDecimal calcTotal(OrderDemo orderDemo) {
        BigDecimal totalPrice = orderDemo.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalPrice;
    }
}
