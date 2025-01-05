package com.example.productorderingsystem.repository;

import com.example.productorderingsystem.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepo extends MongoRepository<Category, String> {
    
}