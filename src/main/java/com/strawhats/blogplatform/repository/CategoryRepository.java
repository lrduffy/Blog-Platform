package com.strawhats.blogplatform.repository;

import com.strawhats.blogplatform.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findCategoryByCategoryName(String categoryName);

    Page<Category> findAllByCategoryNameLike(String categoryName);

    boolean existsCategoryByCategoryId(Long categoryId);

    Optional<Category> findCategoryByCategoryNameIgnoreCase(String categoryName);
}
