package com.example.productorderingsystem.entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "categories")
public class Category {

     @Id
    private String id; // Use String for MongoDB ObjectId
    private BigDecimal totalPrice;

    @DBRef // Use DBRef if you want to reference another document
    private List<OrderItem> orderItemList;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private String name;
 
    
}
