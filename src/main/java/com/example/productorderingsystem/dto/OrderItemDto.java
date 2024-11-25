package com.example.productorderingsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderItemDto {

    private Long id;
    private int quantity;
    private BigDecimal price;
    private String  status;
    private  UserDto user;
    private ProductDto product;
    private LocalDateTime createdAt;
}
