package com.example.Shop.controller;

import com.example.Shop.model.Role;
import com.example.Shop.model.User;
import com.example.Shop.repository.RoleRepository;
import com.example.Shop.repository.UserRepository;
import com.example.Shop.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/auth")
public class AuthPagesController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/login")
    public String login(){
        return "/auth/login";
    }

    @GetMapping("/register")
    public String register(){
        return "/auth/register";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute User user, Model model) {
        Optional<User> foundUser = userRepository.findUserByUsername(user.getUsername());
        if (foundUser.isPresent()) {
            model.addAttribute("error", "Ім’я користувача вже існує");
            return "/auth/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<Role> role = roleRepository.findByName("ROLE_USER");
        if (role.isEmpty()) {
            Role newUserRole = new Role();
            newUserRole.setName("ROLE_USER");
            roleRepository.save(newUserRole);
            role = Optional.of(newUserRole);
        }
        user.setRoles(Set.of(role.get()));

        userRepository.save(user);
        return "redirect:/auth/login";
    }
}
