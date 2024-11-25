package com.example.productorderingsystem.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "payments")
public class Payment {

    @Id
    private String id; // MongoDB ObjectId

    private BigDecimal amount;
    private String method;
    private String status;

    // Reference to the Order
    @DBRef
    private Order order;

    private final LocalDateTime createdAt = LocalDateTime.now();
}
