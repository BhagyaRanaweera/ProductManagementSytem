package com.example.productorderingsystem.service.impl;

import com.example.productorderingsystem.dto.OrderItemDto;
import com.example.productorderingsystem.dto.OrderRequest;
import com.example.productorderingsystem.dto.Response;
import com.example.productorderingsystem.entity.Order;
import com.example.productorderingsystem.entity.OrderItem;
import com.example.productorderingsystem.entity.Product;
import com.example.productorderingsystem.entity.User;
import com.example.productorderingsystem.enums.OrderStatus;
import com.example.productorderingsystem.exception.NotFoundException;
import com.example.productorderingsystem.mapper.EntityDtoMapper;
import com.example.productorderingsystem.repository.OrderRepo;
import com.example.productorderingsystem.repository.OrderItemRepo;
import com.example.productorderingsystem.repository.ProductRepo;
import com.example.productorderingsystem.service.interf.OrderItemService;
import com.example.productorderingsystem.service.interf.UserService;
import com.example.productorderingsystem.specification.OrderItemSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

   
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductRepo productRepo;
    private final UserService userService;
    private final EntityDtoMapper entityDtoMapper;
    private final MongoTemplate mongoTemplate;

    // Constructor injection (Lombok's @RequiredArgsConstructor will handle this)
    

    @Override
public Response placeOrder(OrderRequest orderRequest) {
    User user = userService.getLoginUser();

    // Map and persist OrderItems
    List<OrderItem> orderItems = orderRequest.getItems().stream().map(orderItemRequest -> {
        Product product = productRepo.findById(orderItemRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemRequest.getQuantity());
        orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity())));
        orderItem.setStatus(OrderStatus.PENDING);
        orderItem.setUser(user);

        return orderItemRepo.save(orderItem); // Persist OrderItem
    }).collect(Collectors.toList());

    // Calculate total price
    BigDecimal totalPrice = orderItems.stream()
            .map(OrderItem::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Create and persist the Order
    Order order = new Order();
    order.setOrderItemList(orderItems);
    order.setTotalPrice(totalPrice);
    Order savedOrder = orderRepo.save(order);

    // Update Order reference in OrderItems
    orderItems.forEach(orderItem -> {
        orderItem.setOrder(savedOrder);
        orderItemRepo.save(orderItem); // Save updated OrderItem
    });

    return Response.builder()
            .status(200)
            .message("Order was successfully placed")
            .build();
}


  
@Override
public Response updateOrderItemStatus(String orderItemId, String status) {
    OrderItem orderItem = orderItemRepo.findById(orderItemId)
            .orElseThrow(()-> new NotFoundException("Order Item not found"));

    orderItem.setStatus(OrderStatus.valueOf(status.toUpperCase()));
    orderItemRepo.save(orderItem);
    return Response.builder()
            .status(200)
            .message("Order status updated successfully")
            .build();
}
    @Override
    public Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, String itemId, Pageable pageable) {
        Query query = new Query();
    
        // Combine criteria
        Criteria criteria = new Criteria().andOperator(
                OrderItemSpecification.hasStatus(status),
                OrderItemSpecification.createdBetween(startDate, endDate),
                OrderItemSpecification.hasItemId(itemId)
        );
        query.addCriteria(criteria);
    
        // Apply pagination
        query.with(pageable);
    
        // Execute the query
        List<OrderItem> orderItems = mongoTemplate.find(query, OrderItem.class);
        long totalElements = mongoTemplate.count(query.skip(-1).limit(-1), OrderItem.class); // Count total elements without pagination
    
        if (orderItems.isEmpty()) {
            throw new NotFoundException("No Order Found");
        }
    
        List<OrderItemDto> orderItemDtos = orderItems.stream()
                .map(entityDtoMapper::mapOrderItemToDtoPlusProductAndUser)
                .collect(Collectors.toList());
    
        return Response.builder()
                .status(200)
                .orderItemList(orderItemDtos)
                .totalPage((int) Math.ceil((double) totalElements / pageable.getPageSize()))
                .totalElement(totalElements)
                .build();
    }
    

}