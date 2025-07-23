package com.strawhats.blogplatform.service;

import com.strawhats.blogplatform.payload.CategoryDTO;
import com.strawhats.blogplatform.payload.PagedResponse;

public class CategoryServiceImpl implements CategoryService {
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        return null;
    }

    @Override
    public PagedResponse<CategoryDTO> getCategories(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        return null;
    }

    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
        return null;
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        return null;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        return null;
    }

    @Override
    public PagedResponse<CategoryDTO> searchCategories(String keyword, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        return null;
    }
}
