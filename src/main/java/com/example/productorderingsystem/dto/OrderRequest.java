package com.example.productorderingsystem.dto;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.example.productorderingsystem.entity.OrderItem;
import com.example.productorderingsystem.entity.Payment;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest {

    private BigDecimal totalPrice;
     private List<OrderItemRequest> items;
    private Payment paymentInfo;
    // private List<OrderItem> items = new ArrayList<>();
}