package com.example.productorderingsystem.security;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.productorderingsystem.entity.User;
import com.example.productorderingsystem.repository.UserRepository;

   
    
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(username)
                .orElseThrow(()-> new NotFoundException("User/ Email Not found"));

        return AuthUser.builder()
                .user(user)
                .build();
    }
}