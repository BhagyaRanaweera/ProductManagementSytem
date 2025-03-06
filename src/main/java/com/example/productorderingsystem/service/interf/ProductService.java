package com.example.productorderingsystem.service.interf;

import com.example.productorderingsystem.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface ProductService {

    Response createProduct(String categoryId, MultipartFile image, String name, String description, BigDecimal price);
    Response updateProduct(String productId, String categoryId, MultipartFile image, String name, String description, BigDecimal price);
    Response deleteProduct(String productId);
    Response getProductById(String productId);
    Response getAllProducts();
    Response getProductsByCategory(String categoryId);
    Response searchProduct(String searchValue);
    Response filterProducts(String categoryId, BigDecimal minPrice, BigDecimal maxPrice, String name, String sortBy, String sortDirection);
    
    
   
       // Response getProductByName(String productName);
    
    
    
    
}
