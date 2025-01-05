package com.example.productorderingsystem.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order_items") // MongoDB collection
public class OrderItemDto {

    @Id
    private String id; // MongoDB uses String for ID by default
    private int quantity;
    private BigDecimal price;
    private String status;

    @DBRef(lazy = true) // Reference to User document
    private UserDto user;

    @DBRef(lazy = true) // Reference to Product document
    private ProductDto product;

    private LocalDateTime createdAt;
}
