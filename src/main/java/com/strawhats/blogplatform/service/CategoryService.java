package com.strawhats.blogplatform.service;

import com.strawhats.blogplatform.payload.CategoryDTO;
import com.strawhats.blogplatform.payload.PagedResponse;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    PagedResponse<CategoryDTO> getCategories(Integer pageNumber, Integer pageSize, String sortField, String sortDirection);

    CategoryDTO getCategoryById(Long categoryId);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    PagedResponse<CategoryDTO> searchCategories(String keyword, Integer pageNumber, Integer pageSize, String sortField, String sortDirection);
}
