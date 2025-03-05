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
        // Get the currently logged-in user
        User user = userService.getLoginUser();
    
        // Map and persist OrderItems
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(orderItemRequest -> {
            // Fetch the product using the provided productId
            Product product = productRepo.findById(orderItemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product Not Found with ID: " + orderItemRequest.getProductId()));
    
            // Create and populate OrderItem entity
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity())));
            orderItem.setStatus(OrderStatus.PENDING);
            orderItem.setUser(user);
    
            // Persist the OrderItem
            return orderItemRepo.save(orderItem);
        }).collect(Collectors.toList());
    
        // Calculate the total price of the order
        BigDecimal totalPrice = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    
        // Create the Order entity
        Order order = new Order();
        order.setOrderItemList(orderItems);
        order.setTotalPrice(totalPrice);
    
        // Persist the Order
        Order savedOrder = orderRepo.save(order);
    
        // Update the Order reference in each OrderItem and persist again
        orderItems.forEach(orderItem -> {
            orderItem.setOrder(savedOrder);
            orderItemRepo.save(orderItem);  // Save updated OrderItem with reference to the Order
        });
    
        // Return success response
        return Response.builder()
                .status(200)
                .message("Order was successfully placed")
                .build();
    }
    
    
    
    @Override
    public Response updateOrderItemStatus(String orderItemId, String status) {
        OrderItem orderItem = orderItemRepo.findById(orderItemId)
                .orElseThrow(() -> new NotFoundException("Order Item not found"));

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

        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }
        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("createdAt").gte(startDate).lte(endDate));
        }
        if (itemId != null && !itemId.isEmpty()) {
            query.addCriteria(Criteria.where("_id").is(itemId));
        }

        List<OrderItem> orderItems = mongoTemplate.find(query, OrderItem.class);

        if (orderItems.isEmpty()) {
            throw new NotFoundException("No Orders Found");
        }

        List<OrderItemDto> orderItemDtos = orderItems.stream()
                .map(entityDtoMapper::mapOrderItemToDtoPlusProductAndUser)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .orderItemList(orderItemDtos)
                .totalElement((long) orderItemDtos.size())
                .build();
    }
    @Override
public Response getUserOrders(Pageable pageable) {
    // Get the currently logged-in user
    User user = userService.getLoginUser();

    // Fetch orders for the logged-in user
    Query query = new Query().addCriteria(Criteria.where("user._id").is(user.getId()));
    query.with(pageable);

    List<OrderItem> orderItems = mongoTemplate.find(query, OrderItem.class);

    if (orderItems.isEmpty()) {
        throw new NotFoundException("No orders found for this user.");
    }

    // Convert entities to DTOs
    List<OrderItemDto> orderItemDtos = orderItems.stream()
            .map(entityDtoMapper::mapOrderItemToDtoPlusProductAndUser)
            .collect(Collectors.toList());

    return Response.builder()
            .status(200)
            .orderItemList(orderItemDtos)
            .totalElement((long) orderItemDtos.size())
            .build();
}

}
