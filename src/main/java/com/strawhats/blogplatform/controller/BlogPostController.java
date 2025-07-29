package com.strawhats.blogplatform.controller;

import com.strawhats.blogplatform.config.AppConstants;
import com.strawhats.blogplatform.payload.BlogPostDTO;
import com.strawhats.blogplatform.payload.PagedResponse;
import com.strawhats.blogplatform.service.BlogPostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/blogposts")
public class BlogPostController {

    private final BlogPostService blogPostService;

    @PostMapping
    public ResponseEntity<BlogPostDTO> createBlogPost(BlogPostDTO blogPostDTO) {
        BlogPostDTO createBlogPostDTO = blogPostService.createBlogPost(blogPostDTO);
        return new ResponseEntity<>(createBlogPostDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<BlogPostDTO>> getBlogPosts(
            @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(value = "sortField", required = false, defaultValue = AppConstants.BLOGPOST_SORT_FIELD) String sortField,
            @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ) {
        PagedResponse<BlogPostDTO> blogPostDTOPagedResponse = blogPostService.getBlogPosts(pageNumber, pageSize, sortField, sortDirection);
        return new ResponseEntity<>(blogPostDTOPagedResponse, HttpStatus.OK);
    }

    @GetMapping("/{blogPostId}")
    public ResponseEntity<BlogPostDTO> getBlogPostById(@PathVariable Long blogPostId) {
        BlogPostDTO blogPostDTO = blogPostService.getBlogPostById(blogPostId);
        return new ResponseEntity<>(blogPostDTO, HttpStatus.OK);
    }

    @PutMapping("/{blogPostId}")
    public ResponseEntity<BlogPostDTO> updateBlogPost(@PathVariable Long blogPostId, @RequestBody BlogPostDTO blogPostDTO) {
        BlogPostDTO updateBlogPostDTO = blogPostService.updateBlogPost(blogPostId, blogPostDTO);
        return new ResponseEntity<>(updateBlogPostDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{blogPostId}")
    public ResponseEntity<BlogPostDTO> deleteBlogPost(@PathVariable Long blogPostId) {
        BlogPostDTO deleteBlogPostDTO = blogPostService.deleteBlogPost(blogPostId);
        return new ResponseEntity<>(deleteBlogPostDTO, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponse<BlogPostDTO>> searchBlogPosts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(value = "sortField", required = false, defaultValue = AppConstants.BLOGPOST_SORT_FIELD) String sortField,
            @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ) {
        PagedResponse<BlogPostDTO> blogPostDTOPagedResponse = blogPostService.searchBlogPosts(keyword, pageNumber, pageSize, sortField, sortDirection);
        return new ResponseEntity<>(blogPostDTOPagedResponse, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<PagedResponse<BlogPostDTO>> getBlogPostsByCategoryName(
            @PathVariable String categoryName,
            @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(value = "sortField", required = false, defaultValue = AppConstants.BLOGPOST_SORT_FIELD) String sortField,
            @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ) {
        PagedResponse<BlogPostDTO> blogPostDTOPagedResponse = blogPostService.getBlogPostsByCategoryName(categoryName, pageNumber, pageSize, sortField, sortDirection);
        return new ResponseEntity<>(blogPostDTOPagedResponse, HttpStatus.OK);
    }

    @GetMapping("/author/{authorName}")
    public ResponseEntity<PagedResponse<BlogPostDTO>> getBlogPostsByAuthorName(
            @PathVariable String categoryName,
            @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(value = "sortField", required = false, defaultValue = AppConstants.BLOGPOST_SORT_FIELD) String sortField,
            @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ) {
       PagedResponse<BlogPostDTO> blogPostDTOPagedResponse = blogPostService.getBlogPostsByAuthorName(categoryName, pageNumber, pageSize, sortField, sortDirection);
       return new ResponseEntity<>(blogPostDTOPagedResponse, HttpStatus.OK);
    }
}
