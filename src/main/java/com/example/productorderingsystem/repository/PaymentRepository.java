package com.example.productorderingsystem.repository;

import com.example.productorderingsystem.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {
}
