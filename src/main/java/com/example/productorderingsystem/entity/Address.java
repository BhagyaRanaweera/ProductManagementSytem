
package com.example.productorderingsystem.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.Data;
import lombok.ToString;

@Data
@Document(collection = "addresses") // MongoDB-specific annotation
@ToString(exclude = "user")
public class Address {

    @Id
    private String id; // MongoDB uses String (ObjectId) for IDs by default

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    @DBRef // Reference to another MongoDB document (User)
    private User user;

    private final LocalDateTime createdAt = LocalDateTime.now();
}
