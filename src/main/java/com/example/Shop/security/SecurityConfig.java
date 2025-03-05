package com.example.Shop.security;

import com.example.Shop.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Позначає клас як конфігураційний компонент Spring Security
public class SecurityConfig {
    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Фільтр для обробки JWT-токенів
    @Autowired
    private final UserDetailsService userDetailsService; // Сервіс для отримання користувачів

    // Конструктор класу, який отримує необхідні залежності через ін'єкцію
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean // Створює та повертає об'єкт для кодування паролів
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Використовує алгоритм BCrypt для хешування паролів
    }

    @Bean // Створює менеджер аутентифікації, який використовується для перевірки облікових даних користувача
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean // Налаштовує ланцюг безпеки для обробки HTTP-запитів
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Вимикає CSRF-захист (для REST API зазвичай не потрібен)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login").permitAll() // Дозволяє доступ без аутентифікації до реєстрації та входу
                        .requestMatchers("/products/**").hasAnyRole("ADMIN", "MANAGER") // Доступ до продуктів тільки для адміністраторів і менеджерів
                        .anyRequest().authenticated() // Всі інші запити вимагають аутентифікації
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Додає фільтр JWT перед стандартним фільтром аутентифікації

        return http.build(); // Будує конфігурацію безпеки
    }

    @Bean // Створює та налаштовує провайдера аутентифікації
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Встановлює сервіс для отримання користувачів
        authProvider.setPasswordEncoder(passwordEncoder()); // Встановлює кодувальник паролів
        return authProvider;
    }
}