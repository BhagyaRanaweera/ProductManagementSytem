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
    private String id; 

    private BigDecimal totalPrice;

    
    @DBRef 
    private List<OrderItem> orderItemList;

    private final LocalDateTime createdAt = LocalDateTime.now();

    // Additional fields for payment
}
