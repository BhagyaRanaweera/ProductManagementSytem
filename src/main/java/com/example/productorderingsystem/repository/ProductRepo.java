package com.example.productorderingsystem.repository;


import com.example.productorderingsystem.dto.Response;
import com.example.productorderingsystem.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query("SELECT p FROM Product p WHERE (:categoryId IS NULL OR p.category.id = :categoryId) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:name IS NULL OR p.name LIKE %:name%)")
    List<Product> findProducts(@Param("categoryId") String categoryId,
                               @Param("minPrice") BigDecimal minPrice,
                               @Param("maxPrice") BigDecimal maxPrice,
                               @Param("name") String name,
                               Sort sort);

                            //   Product findByName(String name);
}
