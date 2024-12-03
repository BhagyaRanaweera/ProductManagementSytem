package com.example.productorderingsystem.service.interf;
import com.example.productorderingsystem.dto.OrderRequest;
import com.example.productorderingsystem.dto.Response;
import com.example.productorderingsystem.enums.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface OrderItemService {
    Response placeOrder(OrderRequest orderRequest);
    Response updateOrderItemStatus(Long orderItemId, String status);
    Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable);
}

