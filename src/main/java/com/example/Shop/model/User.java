package com.example.Shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_ id")
    )
    private Set<Role> roles;

    public User(long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {}
}
