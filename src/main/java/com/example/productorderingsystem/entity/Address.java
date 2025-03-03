package com.example.productorderingsystem.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;



@Data
@Document(collection = "addresses") // MongoDB collection name
public class Address {

    @Id
    private String id;  // MongoDB will automatically use this as the document ID
    
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    
    private User user;
    
    private LocalDateTime createdAt;
}