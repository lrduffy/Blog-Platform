package com.strawhats.blogplatform.repository;

import com.strawhats.blogplatform.model.BlogPost;
import com.strawhats.blogplatform.model.Category;
import com.strawhats.blogplatform.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost,Long>, JpaSpecificationExecutor<BlogPost> {

    @Query("SELECT blogPost FROM BlogPost blogPost WHERE blogPost.title = :title AND blogPost.author.userId = :authorId")
    Optional<BlogPost> findBlogPostByTitleAndAuthorId(@Param("title") String title, @Param("authorId") Long authorId);

    Optional<BlogPost> findBlogPostByBlogPostId(Long blogPostId);

    Page<BlogPost> findBlogPostsByCategory(Category category, Pageable pageable);

    Page<BlogPost> findBlogPostsByAuthor(User author, Pageable pageable);
}
