package com.example.Shop.controller;

import com.example.Shop.model.User;
import com.example.Shop.repository.UserRepository;
import com.example.Shop.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthPagesController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login(){
        return "/auth/login";
    }

    @GetMapping("/register")
    public String register(){
        return "/auth/register";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/auth/login";
    }
}
