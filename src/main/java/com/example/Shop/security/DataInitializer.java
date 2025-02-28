package com.example.Shop.security;

import com.example.Shop.model.Role;
import com.example.Shop.model.User;
import com.example.Shop.repository.RoleRepository;
import com.example.Shop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.findByName("ADMIN").isEmpty()){
            // Create first admin role
            Role role = new Role();
            role.setName("ADMIN");
            roleRepository.save(role);

            // Create first admin user
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("12345"));
            user.setEmail("admin@gmail.com");
            user.setRoles(Set.of(role));
            userRepository.save(user);
        }
    }
}
