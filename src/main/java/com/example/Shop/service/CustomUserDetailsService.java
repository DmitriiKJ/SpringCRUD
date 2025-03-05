package com.example.Shop.service;

import com.example.Shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return userRepository.findUserByUsername(username)
                .map(user -> {
                    if(!user.isEnabled()){
                        throw new UsernameNotFoundException("User " + username + " is not found!");
                    }
                    if(!user.isAccountNonExpired()){
                        throw new UsernameNotFoundException("User " + username + " is expired!");
                    }
                    if(!user.isCredentialsNonExpired()){
                        throw new UsernameNotFoundException("User " + username + " credentials is expired!");
                    }
                    if(!user.isAccountNonLocked()){
                        throw new UsernameNotFoundException("User " + username + " locked!");
                    }
                    return user;
                }).orElseThrow(() -> new UsernameNotFoundException("User " + username + " is not found!"));
    }
}
