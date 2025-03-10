package com.example.Shop.controller;

import com.example.Shop.model.OrderDemo;
import com.example.Shop.model.OrderItem;
import com.example.Shop.model.User;
import com.example.Shop.repository.OrderDemoRepository;
import com.example.Shop.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class RestOrderDemoController {
    private final OrderDemoRepository orderDemoRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public RestOrderDemoController(OrderDemoRepository orderDemoRepository, OrderItemRepository orderItemRepository) {
        this.orderDemoRepository = orderDemoRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrders(){
        List<OrderDemo> orderDemos = orderDemoRepository.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("count", orderDemos.size());
        response.put("orders", orderDemos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable long id){
        Optional<OrderDemo> orderDemo = orderDemoRepository.findById(id);
        Map<String, Object> response = new HashMap<>();
        if(orderDemo.isPresent()){
            response.put("order", orderDemo.get());
            response.put("status", "success");
            return ResponseEntity.ok(response);
        }
        else {
            response.put("status", "error");
            response.put("message", "Not found");
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderDemo orderDemo){
        Map<String, Object> response = new HashMap<>();
        if(orderDemo.getOrderItems() == null || orderDemo.getOrderItems().isEmpty()){
            response.put("status", "error");
            response.put("message", "Order must contain at least one item");
            return ResponseEntity.badRequest().build();
        }

        BigDecimal totalPrice = orderDemo.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderDemo.setTotal(totalPrice);

        OrderDemo savedOrderDemo = orderDemoRepository.save(orderDemo);

        orderDemo.getOrderItems().stream()
                        .forEach(item -> {
                            item.setOrder(savedOrderDemo);
                            orderItemRepository.save(item);
                        });

        OrderDemo orderDemoSaved = orderDemoRepository.save(orderDemo);
        response.put("order", orderDemoSaved);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PostMapping("/{id}")
    public ResponseEntity<Map<String, Object>> addItemToOrder(@PathVariable long id, @RequestBody OrderItem newItem) {
        Map<String, Object> response = new HashMap<>();
        Optional<OrderDemo> orderDemo = orderDemoRepository.findById(id);
        if(orderDemo.isEmpty()){
            response.put("status", "error");
            response.put("message", "Order with this id doesn't exist");
            return ResponseEntity.badRequest().body(response);
        }

        OrderDemo orderDemoGet = orderDemo.get();
        if (newItem.getQuantity() <= 0 || newItem.getPrice().compareTo(BigDecimal.ZERO) <= 0){
            response.put("status", "error");
            response.put("message", "Incorrect data in order item");
            return ResponseEntity.badRequest().body(response);
        }

        orderDemoGet.setTotal(orderDemoGet.getTotal().add(newItem.getPrice().multiply(BigDecimal.valueOf(newItem.getQuantity()))));

        newItem.setOrder(orderDemoGet);
        orderDemoGet.getOrderItems().add(newItem);
        orderItemRepository.save(newItem);
        orderDemoRepository.save(orderDemoGet);

        response.put("order", orderDemoGet);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateOrder(@PathVariable long id, @RequestBody OrderDemo orderDemo){
        Map<String, Object> response = new HashMap<>();
        if(orderDemo.getOrderItems() == null || orderDemo.getOrderItems().isEmpty()){
            response.put("status", "error");
            response.put("message", "Order must contain at least one item");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<OrderDemo> oldOrderOpt = orderDemoRepository.findById(id);
        if(oldOrderOpt.isEmpty()){
            response.put("status", "error");
            response.put("message", "Order doesn't exist");
            return ResponseEntity.badRequest().body(response);
        }

        OrderDemo oldOrder = oldOrderOpt.get();

        BigDecimal totalPrice = orderDemo.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        oldOrder.setTotal(totalPrice);
        oldOrder.getOrderItems().clear();
        oldOrder.setUser(orderDemo.getUser());

        orderDemo.getOrderItems().stream()
                .forEach(item -> {
                    item.setOrder(oldOrder);
                    orderItemRepository.save(item);
                    oldOrder.getOrderItems().add(item);
                });

        OrderDemo savedOrderDemo = orderDemoRepository.save(oldOrder);

        response.put("order", savedOrderDemo);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}
