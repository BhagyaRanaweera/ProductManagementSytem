package com.example.productorderingsystem.repository;


import com.example.productorderingsystem.entity.Order;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface OrderRepo extends MongoRepository<Order, String> {
    
   
}