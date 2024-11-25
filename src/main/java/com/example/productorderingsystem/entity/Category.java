package com.example.productorderingsystem.entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "categories")
public class Category {

    @Id
    private String id; // MongoDB IDs are typically String (ObjectId)

    private String name;

    // Use @DBRef to reference related Product documents
    @DBRef
    private List<Product> productList;

    private final LocalDateTime createdAt = LocalDateTime.now();
}
