package com.example.Shop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // Hash
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable())
                // pages which are enable without login
                .authorizeHttpRequests(auth -> auth.requestMatchers(
                        "/",
                        "/auth/register",
                        "/auth/login",
                        "/products/read",
                        "/api/products"
                )
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                // custom login
                .formLogin(form -> form.loginPage("/auth/login").defaultSuccessUrl("/", false)
                        .permitAll())
                // custom logout
                .logout(form -> form.logoutUrl("/auth/logout").permitAll());

        return http.build();
    }
}
