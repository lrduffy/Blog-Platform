package com.strawhats.blogplatform.service;

import com.strawhats.blogplatform.exception.ApiException;
import com.strawhats.blogplatform.exception.ResourceNotFoundException;
import com.strawhats.blogplatform.model.Category;
import com.strawhats.blogplatform.payload.CategoryDTO;
import com.strawhats.blogplatform.payload.PagedResponse;
import com.strawhats.blogplatform.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Optional<Category> categoryOptional = categoryRepository.findCategoryByCategoryName(categoryDTO.getName());
        if (categoryOptional.isPresent()) {
            throw new ApiException("Category already exists");
        }

        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);

        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
        return savedCategoryDTO;
    }

    @Override
    public PagedResponse<CategoryDTO> getCategories(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<CategoryDTO> content = categoryPage
                .stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        PagedResponse<CategoryDTO> categoryDTOPagedResponse = new PagedResponse<>();
        categoryDTOPagedResponse.setContent(content);
        categoryDTOPagedResponse.setPageNumber(categoryPage.getNumber());
        categoryDTOPagedResponse.setPageSize(categoryPage.getSize());
        categoryDTOPagedResponse.setTotalPages(categoryPage.getTotalPages());
        categoryDTOPagedResponse.setTotalElements(categoryPage.getTotalElements());
        categoryDTOPagedResponse.setIsLastPage(categoryPage.isLast());

        return categoryDTOPagedResponse;
    }

    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

        return categoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        category.setCategoryName(categoryDTO.getName());
        Category savedCategory = categoryRepository.save(category);

        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);

        return savedCategoryDTO;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        categoryRepository.delete(category);

        CategoryDTO deleteCategoryDTO = modelMapper.map(category, CategoryDTO.class);

        return deleteCategoryDTO;
    }

    @Override
    public PagedResponse<CategoryDTO> searchCategories(String keyword, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ?  Sort.by(sortField).ascending() : Sort.by(sortField);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        String pattern = "%" + keyword + "%";
        Page<Category> categoryPage = keyword != null ? categoryRepository.findAllByCategoryNameLike(pattern, pageable) : categoryRepository.findAll(pageable);

        List<CategoryDTO> content = categoryPage
                .stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        PagedResponse<CategoryDTO> categoryDTOPagedResponse = new PagedResponse<>();
        categoryDTOPagedResponse.setContent(content);
        categoryDTOPagedResponse.setPageNumber(categoryPage.getNumber());
        categoryDTOPagedResponse.setPageSize(categoryPage.getSize());
        categoryDTOPagedResponse.setTotalPages(categoryPage.getTotalPages());
        categoryDTOPagedResponse.setTotalElements(categoryPage.getTotalElements());
        categoryDTOPagedResponse.setIsLastPage(categoryPage.isLast());

        return categoryDTOPagedResponse;
    }
}
