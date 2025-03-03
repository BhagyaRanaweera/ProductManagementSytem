package com.example.productorderingsystem.repository;


import com.example.productorderingsystem.dto.Response;
import com.example.productorderingsystem.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.util.List;
//import java.util.Optional;

public interface ProductRepo extends MongoRepository<Product,String> {
    List<Product> findByCategoryId(String categoryId);
    List<Product> findByNameContainingOrDescriptionContaining(String name, String description);
@Query("{ $and: ["
            + "{ $or: [ { 'category.id': ?0 }, { ?0: null } ] },"
            + "{ $or: [ { 'price': { $gte: ?1 } }, { ?1: null } ] },"
            + "{ $or: [ { 'price': { $lte: ?2 } }, { ?2: null } ] },"
            + "{ $or: [ { 'name': { $regex: ?3, $options: 'i' } }, { ?3: null } ] }"
            + "] }")
    List<Product> findByFilters(String categoryId, BigDecimal minPrice, BigDecimal maxPrice, String name);

}
