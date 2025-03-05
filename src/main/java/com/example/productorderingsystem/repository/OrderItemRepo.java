package com.example.productorderingsystem.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import com.example.productorderingsystem.entity.OrderItem;

@Repository
public interface OrderItemRepo extends MongoRepository<OrderItem, String> {

    @SuppressWarnings("null")
    Page<OrderItem> findAll(Pageable pageable);  // Remove Specification, use Pageable only
}
