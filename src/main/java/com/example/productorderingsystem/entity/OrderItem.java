package com.example.productorderingsystem.entity;

import com.example.productorderingsystem.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order_items")
public class OrderItem {
    @Id
    private String id; // MongoDB ObjectId as String

    private int quantity;
    private BigDecimal price;
    private OrderStatus status;

    @DBRef
    private User user;

    @DBRef
    private Product product;

    @DBRef
    private Order order;

    private final LocalDateTime createdAt = LocalDateTime.now();

    // ✅ Fixed `getProductId` method
    public String getProductId() {
        return product != null ? product.getId() : null;
    }

    
}
