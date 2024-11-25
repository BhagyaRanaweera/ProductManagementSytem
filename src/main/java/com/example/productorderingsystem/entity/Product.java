package com.example.productorderingsystem.entity;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id; // MongoDB IDs are typically Strings for ObjectId

    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;

    // Use @DBRef to reference the Category document
    @DBRef
    private Category category;

    private final LocalDateTime createdAt = LocalDateTime.now();
}
