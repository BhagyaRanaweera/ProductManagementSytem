package com.example.productorderingsystem.dto;


// ProductResponse.java (DTO)
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

import com.example.productorderingsystem.entity.Category;
import com.example.productorderingsystem.entity.Product;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private Category category;  // The category object
    private List<Product> products;  // List of products for the given category
}
