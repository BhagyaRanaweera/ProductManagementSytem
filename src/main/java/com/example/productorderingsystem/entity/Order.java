package com.example.productorderingsystem.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "orders")
public class Order {

    @Id
    private String id; // MongoDB uses String for ID by default
    private BigDecimal totalPrice;

    // @DBRef used for referencing other documents (OrderItem in this case)
    @DBRef(lazy = true) 
    private List<OrderItem> orderItemList;

    private final LocalDateTime createdAt = LocalDateTime.now();

    // Payment details would be added here
}
