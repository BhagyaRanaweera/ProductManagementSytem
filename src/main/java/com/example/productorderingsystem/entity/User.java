package com.example.productorderingsystem.entity;
import com.example.productorderingsystem.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Document(collection = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;

    private String name;
    
    private String email;

    private String password;

    private String phoneNumber;

    private UserRole role;

    @DBRef
    private List<OrderItem> orderItemList;

    @DBRef
    private Address address;

}
