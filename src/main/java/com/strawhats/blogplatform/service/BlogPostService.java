package com.strawhats.blogplatform.service;

import com.strawhats.blogplatform.payload.BlogPostDTO;
import com.strawhats.blogplatform.payload.PagedResponse;

public interface BlogPostService {

    BlogPostDTO createBlogPost(BlogPostDTO blogPostDTO);

    PagedResponse<BlogPostDTO> getBlogPosts(Integer pageNumber, Integer pageSize, String sortField, String sortDirection);

    BlogPostDTO getBlogPostById(Long id);

    BlogPostDTO updateBlogPost(Long id, BlogPostDTO blogPostDTO);

    BlogPostDTO deleteBlogPost(Long id);

    PagedResponse<BlogPostDTO> searchBlogPosts(String keyword, Integer pageNumber, Integer pageSize, String sortField, String sortDirection);

    PagedResponse<BlogPostDTO> getBlogPostsByCategoryName(String categoryName, Integer pageNumber, Integer pageSize, String sortField, String sortDirection);

    PagedResponse<BlogPostDTO> getBlogPostsByAuthorName(String authorName, Integer pageNumber, Integer pageSize, String sortField, String sortDirection);
}
