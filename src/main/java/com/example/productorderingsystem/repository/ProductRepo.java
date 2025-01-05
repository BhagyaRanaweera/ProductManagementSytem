package com.example.productorderingsystem.repository;


import com.example.productorderingsystem.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
//import java.util.Optional;

public interface ProductRepo extends MongoRepository<Product,String> {
    List<Product> findByCategoryId(String categoryId);
    List<Product> findByNameContainingOrDescriptionContaining(String name, String description);


    
}
