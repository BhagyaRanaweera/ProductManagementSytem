package com.example.productorderingsystem.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "reviews")
public class Review {

    @Id
    private String id; // MongoDB ObjectId

    private String content;
    private int rating; // Assuming a scale of 1 to 10

    // Reference to the Product
    @DBRef
    private Product product;

    // Reference to the User
    @DBRef
    private User user;

    private final LocalDateTime createdAt = LocalDateTime.now();
}

