package com.example.productorderingsystem.repository;



import com.example.productorderingsystem.entity.OrderItem;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderItemRepo extends MongoRepository<OrderItem,String> {
    // Custom queries can be defined here if needed, such as finding by order ID or product ID
    List<OrderItem> findByOrderId(Long orderId);
    List<OrderItem> findByProductId(Long productId);
}
