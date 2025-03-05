package com.example.Shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Entity
public class User implements UserDetails {
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

    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private LocalDateTime accountExpirationDate;
    private LocalDateTime credentialsExpirationDate;

    public User(long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(roles == null || roles.isEmpty())  return Set.of(new SimpleGrantedAuthority("USER"));

        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        if(accountExpirationDate == null) {
            return true;
        }
        return LocalDateTime.now().isBefore(accountExpirationDate);
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if(credentialsExpirationDate == null){
            return true;
        }
        return LocalDateTime.now().isBefore(credentialsExpirationDate);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
