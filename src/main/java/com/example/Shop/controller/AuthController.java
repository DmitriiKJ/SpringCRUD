package com.example.Shop.controller;

import com.example.Shop.model.Role;
import com.example.Shop.model.User;
import com.example.Shop.repository.RoleRepository;
import com.example.Shop.repository.UserRepository;
import com.example.Shop.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user){
        Map<String, Object> response = new HashMap<>();
        if(userRepository.findUserByUsername(user.getUsername()).isPresent()){
            response.put("success", false);
            response.put("message", "Username is already taken");
            return ResponseEntity.badRequest().body(response);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> userRole = roleRepository.findByName("USER");
        if(userRole.isEmpty()){
            Role newUserRole = new Role();
            newUserRole.setName("USER");
            roleRepository.save(newUserRole);
            userRole = Optional.of(newUserRole);
        }
        user.setRoles(Set.of(userRole.get()));
        userRepository.save(user);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user){
        Map<String, Object> response = new HashMap<>();
        Optional<User> foundUser = userRepository.findUserByUsername(user.getUsername());
        if(foundUser.isEmpty() || !passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())){
            response.put("success", false);
            response.put("message", "Username or password are incorrect");
            return ResponseEntity.badRequest().body(response);
        }

        String token = jwtService.generateJwtToken(foundUser.get());
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}
