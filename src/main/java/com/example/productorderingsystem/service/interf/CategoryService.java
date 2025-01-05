package com.example.productorderingsystem.service.interf;

import com.example.productorderingsystem.dto.CategoryDto;
import com.example.productorderingsystem.dto.Response;

public interface CategoryService {

    Response createCategory(CategoryDto categoryRequest);
    Response updateCategory(String categoryId, CategoryDto categoryRequest);
    Response getAllCategories();
    Response getCategoryById(String categoryId);
    Response deleteCategory(String categoryId);
}