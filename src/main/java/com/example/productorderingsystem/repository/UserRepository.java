package com.example.productorderingsystem.repository;

import com.example.productorderingsystem.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    Object findByEmail(String username);
   
}
