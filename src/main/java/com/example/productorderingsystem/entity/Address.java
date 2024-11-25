package com.example.productorderingsystem.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Data
@Document(collection = "addresses")
public class Address {

    @Id
    private String id;

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    // Use @DBRef to reference the User document
    @DBRef
    private User user;

    private final LocalDateTime createdAt = LocalDateTime.now();
}
