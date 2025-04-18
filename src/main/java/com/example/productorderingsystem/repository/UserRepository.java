package com.example.productorderingsystem.repository;

import com.example.productorderingsystem.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    
}