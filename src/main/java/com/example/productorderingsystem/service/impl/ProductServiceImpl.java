package com.example.productorderingsystem.service.impl;

import com.example.productorderingsystem.dto.CategoryDto;
import com.example.productorderingsystem.dto.ProductDto;
import com.example.productorderingsystem.dto.Response;
import com.example.productorderingsystem.entity.Category;
import com.example.productorderingsystem.entity.Product;
import com.example.productorderingsystem.exception.NotFoundException;
import com.example.productorderingsystem.mapper.EntityDtoMapper;
import com.example.productorderingsystem.repository.CategoryRepo;
import com.example.productorderingsystem.repository.ProductRepo;
import com.example.productorderingsystem.service.AwsS3Service;
import com.example.productorderingsystem.service.interf.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;



    @Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final EntityDtoMapper entityDtoMapper;
    private final AwsS3Service awsS3Service;

    private final MongoTemplate mongoTemplate; // Inject MongoTemplate


    @Override
    public Response createProduct(String categoryId, MultipartFile image, String name, String description, BigDecimal price) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new NotFoundException("Category not found"));
        String productImageUrl = awsS3Service.saveImageToS3(image);

        Product product = new Product();
        product.setCategory(category);
        product.setPrice(price);
        product.setName(name);
        product.setDescription(description);
        product.setImageUrl(productImageUrl);

        productRepo.save(product);
        return Response.builder()
                .status(200)
                .message("Product successfully created")
                .build();
    }

    @Override
    public Response updateProduct(String productId, String categoryId, MultipartFile image, String name, String description, BigDecimal price) {
        Product product = productRepo.findById(productId).orElseThrow(()-> new NotFoundException("Product Not Found"));

        Category category = null;
        String productImageUrl = null;

        if(categoryId != null ){
             category = categoryRepo.findById(categoryId).orElseThrow(()-> new NotFoundException("Category not found"));
        }
        if (image != null && !image.isEmpty()){
            productImageUrl = awsS3Service.saveImageToS3(image);
        }

        if (category != null) product.setCategory(category);
        if (name != null) product.setName(name);
        if (price != null) product.setPrice(price);
        if (description != null) product.setDescription(description);
        if (productImageUrl != null) product.setImageUrl(productImageUrl);

        productRepo.save(product);
        return Response.builder()
                .status(200)
                .message("Product updated successfully")
                .build();

    }

    @Override
    public Response deleteProduct(String productId) {
        Product product = productRepo.findById(productId).orElseThrow(()-> new NotFoundException("Product Not Found"));
        productRepo.delete(product);

        return Response.builder()
                .status(200)
                .message("Product deleted successfully")
                .build();
    }

    @Override
    public Response getProductById(String productId) {
        Product product = productRepo.findById(productId).orElseThrow(()-> new NotFoundException("Product Not Found"));
        ProductDto productDto = entityDtoMapper.mapProductToDtoBasic(product);

        return Response.builder()
                .status(200)
                .product(productDto)
                .build();
    }

    @Override
    public Response getAllProducts() {
        List<ProductDto> productList = productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .productList(productList)
                .build();

    }

    @Override
    public Response getProductsByCategory(String categoryId) {
        List<Product> products = productRepo.findByCategoryId(categoryId);
        if(products.isEmpty()){
            throw new NotFoundException("No Products found for this category");
        }
        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();

    }

    @Override
    public Response searchProduct(String searchValue) {
        List<Product> products = productRepo.findByNameContainingOrDescriptionContaining(searchValue, searchValue);

        if (products.isEmpty()){
            throw new NotFoundException("No Products Found");
        }
        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());


        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }

    @Override
    public Response filterProducts(String categoryId, BigDecimal minPrice, BigDecimal maxPrice, String name, String sortBy, String sortDirection) {
        Query query = new Query();
    
        // Filter by category if categoryId is provided
        if (categoryId != null) {
            query.addCriteria(Criteria.where("category.id").is(categoryId));
        }
    
        // Filter by price range (minPrice and maxPrice)
        if (minPrice != null || maxPrice != null) {
            Criteria priceCriteria = Criteria.where("price");
            if (minPrice != null) {
                priceCriteria = priceCriteria.gte(minPrice); // Greater than or equal to minPrice
            }
            if (maxPrice != null) {
                priceCriteria = priceCriteria.lte(maxPrice); // Less than or equal to maxPrice
            }
            query.addCriteria(priceCriteria); // Add combined price range criteria
        }
    
        // Filter by product name if a name is provided
        if (name != null && !name.isEmpty()) {
            query.addCriteria(Criteria.where("name").regex(".*" + name + ".*", "i")); // Case-insensitive regex for name
        }
    
        // Sorting based on sortBy field and sortDirection (ASC/DESC)
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        query.with(Sort.by(direction, sortBy)); // Apply sorting based on user input
    
        // Query the database with the constructed filter and sorting
        List<Product> products = mongoTemplate.find(query, Product.class);
    
        if (products.isEmpty()) {
            throw new NotFoundException("No products found matching the criteria");
        }
    
        // Map the products to DTOs
        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());
    
        // Return the response with the filtered and sorted product list
        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }

   
    
   

   

    // @Override
    // public Response getProductByName(String productName) {
    //     Product product = productRepo.findByName(productName);

    //     if (product != null) {
    //         // Map Product to ProductDto without modifying CategoryDto
    //         ProductDto productDto = new ProductDto(
    //                 product.getId(),
    //                 product.getName(),
    //                 product.getDescription(),
    //                 product.getPrice(),
    //                 product.getImageUrl(),
    //                 new CategoryDto(product.getCategoryId())  // Just pass the categoryId to CategoryDto
    //         );

    //         return Response.builder()
    //                 .status(200)
    //                 .message("Product found")
    //                 .product(productDto)
    //                 .build();
    //     } else {
    //         return Response.builder()
    //                 .status(404)
    //                 .message("Product not found")
    //                 .build();
    //     }
    // }


    
}