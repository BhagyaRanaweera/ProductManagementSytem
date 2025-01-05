package com.example.productorderingsystem.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import com.example.productorderingsystem.entity.OrderItem;
@Repository
public interface OrderItemRepo extends MongoRepository<OrderItem, String> {
    // Add custom query methods if needed
}
