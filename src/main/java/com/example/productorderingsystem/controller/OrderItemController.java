package com.example.productorderingsystem.controller;

import com.example.productorderingsystem.dto.OrderRequest;
import com.example.productorderingsystem.dto.Response;
import com.example.productorderingsystem.enums.OrderStatus;
import com.example.productorderingsystem.service.interf.OrderItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Import Slf4j for logging

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j // Add this annotation for logger
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping("/create")
    public ResponseEntity<Response> placeOrder(@RequestBody OrderRequest orderRequest){
       
        return ResponseEntity.ok(orderItemService.placeOrder(orderRequest));
    }

    @PutMapping("/update-item-status/{orderItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateOrderItemStatus(@PathVariable String orderItemId, @RequestParam String status) {
        log.info("Received request to update status for OrderItemId: {} to Status: {}", orderItemId, status);
        Response response = orderItemService.updateOrderItemStatus(orderItemId, status);
        log.info("Status update response: {}", response.getMessage());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> filterOrderItems(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String itemId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
            OrderStatus orderStatus = (status != null) ? OrderStatus.valueOf(status.toUpperCase()) : null;
            log.info("Filtering order items with status: {}, startDate: {}, endDate: {}, itemId: {}", status, startDate, endDate, itemId);

            return ResponseEntity.ok(orderItemService.filterOrderItems(orderStatus, startDate, endDate, itemId, pageable));
        } catch (IllegalArgumentException e) {
            log.error("Invalid order status provided: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                Response.builder()
                        .status(400)
                        .message("Invalid order status provided")
                        .build()
            );
        }
    }
    @GetMapping("/my-orders")
public ResponseEntity<Response> getUserOrderHistory(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    return ResponseEntity.ok(orderItemService.getUserOrders(pageable));
}

}
