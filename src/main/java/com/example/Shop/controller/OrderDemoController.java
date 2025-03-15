package com.example.Shop.controller;

import com.example.Shop.model.OrderDemo;
import com.example.Shop.model.User;
import com.example.Shop.repository.OrderDemoRepository;
import com.example.Shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderDemoController {
    @Autowired
    private OrderDemoRepository orderDemoRepository;

    @Autowired
    private UserRepository userRepository;

    // Read
    @GetMapping("/read")
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderDemoRepository.findAll());
        return "order/read";
    }

    @GetMapping("/details")
    public String getOrderById(@RequestParam long id, Model model) {
        var order = orderDemoRepository.findById(id);
        if (order.isEmpty()) {
            return "redirect:/orders/read";
        }
        model.addAttribute("order", order.get());
        return "order/details";
    }

    // Create
    @GetMapping("/create")
    public String addNewOrder(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("userId", user.getId());
        return "order/save";
    }

    @PostMapping("/create")
    public String addNewOrder(OrderDemo orderDemo) {
        orderDemo.setUser(userRepository.findById(orderDemo.getUser().getId()).get());
        orderDemoRepository.save(orderDemo);
        return "redirect:/orders/read";
    }

    // Delete
    @GetMapping("/delete")
    public String deleteOrder(@RequestParam long id, Model model) {
        Optional<OrderDemo> order = orderDemoRepository.findById(id);
        if (order.isEmpty()){
            return "redirect:/orders/read";
        }
        model.addAttribute("order", order.get());
        return "order/delete";
    }

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam long id) {
        orderDemoRepository.deleteById(id);
        return "redirect:/orders/read";
    }
}
