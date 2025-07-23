package com.strawhats.blogplatform.service;

import com.strawhats.blogplatform.payload.BlogPostDTO;
import com.strawhats.blogplatform.payload.PagedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BlogPostServiceImpl implements BlogPostService {
    @Override
    public BlogPostDTO createBlogPost(BlogPostDTO blogPostDTO) {
        return null;
    }

    @Override
    public PagedResponse<BlogPostDTO> getBlogPosts(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        return null;
    }

    @Override
    public BlogPostDTO getBlogPostById(Long id) {
        return null;
    }

    @Override
    public BlogPostDTO updateBlogPost(Long id, BlogPostDTO blogPostDTO) {
        return null;
    }

    @Override
    public BlogPostDTO deleteBlogPost(Long id) {
        return null;
    }

    @Override
    public PagedResponse<BlogPostDTO> searchBlogPosts(String keyword, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        return null;
    }

    @Override
    public PagedResponse<BlogPostDTO> getBlogPostsByCategoryName(String categoryName, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        return null;
    }

    @Override
    public PagedResponse<BlogPostDTO> getBlogPostsByAuthorName(String authorName, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        return null;
    }
}
