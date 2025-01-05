package com.example.productorderingsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderDto {
   
    private String id;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private List<OrderItemDto> orderItemList;
}
