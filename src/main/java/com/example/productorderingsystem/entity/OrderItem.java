package com.example.productorderingsystem.entity;


import com.example.productorderingsystem.enums.OrderStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "order_items")
public class OrderItem {

    @Id
    private String id; // MongoDB ObjectId

    private int quantity;
    private BigDecimal price;
    private OrderStatus status;

    // Reference to the User
    @DBRef
    private User user;

    // Reference to the Product
    @DBRef
    private Product product;

    // Reference to the Order
    @DBRef
    private Order order;

    private final LocalDateTime createdAt = LocalDateTime.now();

    
}
