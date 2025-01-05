package com.example.productorderingsystem.dto;

import lombok.Data;

@Data
public class OrderItemRequest {

    private String productId;
    private int quantity;
}