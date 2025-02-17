package com.example.Shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 2, max = 255, message = "Username min: 2, max: 255")
    @NotBlank(message = "Username can't be null")
    private String username;

    @Size(min = 2, max = 512, message = "Password min: 2, max: 255")
    @NotBlank(message = "Password can't be null")
    private String password;

    @Email(message = "Incorrect email")
    @Size(min = 2, max = 255, message = "Email min: 2, max: 255")
    @NotBlank(message = "Email can't be null")
    private String email;

    @OneToMany(mappedBy = "user")
    private List<OrderDemo> orders;

    public User(long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
