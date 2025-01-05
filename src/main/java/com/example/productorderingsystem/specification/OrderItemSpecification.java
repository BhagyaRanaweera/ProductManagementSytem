package com.example.productorderingsystem.specification;

import com.example.productorderingsystem.enums.OrderStatus;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDateTime;

public class OrderItemSpecification {

    public static Criteria hasStatus(OrderStatus status) {
        if (status != null) {
            return Criteria.where("status").is(status);
        }
        return new Criteria(); // No-op
    }

    public static Criteria createdBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return Criteria.where("createdAt").gte(startDate).lte(endDate);
        } else if (startDate != null) {
            return Criteria.where("createdAt").gte(startDate);
        } else if (endDate != null) {
            return Criteria.where("createdAt").lte(endDate);
        }
        return new Criteria(); // No-op
    }

    public static Criteria hasItemId(String itemId) {
        if (itemId != null && !itemId.isEmpty()) {
            return Criteria.where("id").is(itemId);
        }
        return new Criteria(); // No-op
    }
}
