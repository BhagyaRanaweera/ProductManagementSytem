package com.example.productorderingsystem.entity;


import com.example.productorderingsystem.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order_items")
public class OrderItem {

    @Id
    private String id; // Use String for MongoDB ObjectId

    private int quantity;
    private BigDecimal price;
    private OrderStatus status;

    @DBRef // Use DBRef to reference other documents
    private User user;

    @DBRef // Use DBRef to reference other documents
    private Product product;

    @DBRef // Use DBRef to reference other documents
    private Order order;

    private final LocalDateTime createdAt = LocalDateTime.now(); // Automatically set the creation time
}