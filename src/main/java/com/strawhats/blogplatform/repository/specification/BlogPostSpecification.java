package com.strawhats.blogplatform.repository.specification;

import com.strawhats.blogplatform.model.BlogPost;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BlogPostSpecification {

    public static Specification<BlogPost> blogPostConatainsKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%" + keyword + "%";
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("author").get("username")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("category").get("categoryName")), pattern));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
