package com.strawhats.blogplatform.service;

import com.strawhats.blogplatform.exception.ApiException;
import com.strawhats.blogplatform.exception.ResourceNotFoundException;
import com.strawhats.blogplatform.model.BlogPost;
import com.strawhats.blogplatform.model.Category;
import com.strawhats.blogplatform.model.User;
import com.strawhats.blogplatform.payload.BlogPostDTO;
import com.strawhats.blogplatform.payload.PagedResponse;
import com.strawhats.blogplatform.repository.BlogPostRepository;
import com.strawhats.blogplatform.repository.CategoryRepository;
import com.strawhats.blogplatform.repository.UserRepository;
import com.strawhats.blogplatform.repository.specification.BlogPostSpecification;
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
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public BlogPostDTO createBlogPost(BlogPostDTO blogPostDTO) {
        if (!userRepository.existsUserByUserId(blogPostDTO.getAuthorId())) {
            throw new ResourceNotFoundException("User", "userId", blogPostDTO.getAuthorId());
        }

        if (!categoryRepository.existsCategoryByCategoryId(blogPostDTO.getCategoryId())) {
            throw new ResourceNotFoundException("Category", "categoryId", blogPostDTO.getCategoryId());
        }

        if (blogPostDTO.getTitle() == null || blogPostDTO.getTitle().length() == 0) {
            throw new ApiException("Post title should be specified");
        }

        if (blogPostDTO.getContent() == null || blogPostDTO.getContent().length() == 0) {
            throw new ApiException("Post content should be specified");
        }

        if (blogPostDTO.getCategoryId() == null || blogPostDTO.getCategoryId() == 0) {
            throw new ApiException("Category id should be specified");
        }

        Optional<BlogPost> blogPostOptional = blogPostRepository.findBlogPostByTitleAndAuthorId(blogPostDTO.getTitle(), blogPostDTO.getAuthorId());

        if (blogPostOptional.isPresent()) {
            throw new ApiException("Post with same title already exists");
        }

        BlogPost blogPost = modelMapper.map(blogPostDTO, BlogPost.class);

        Category category = categoryRepository.findById(blogPostDTO.getCategoryId()).get();
        blogPost.setCategory(category);

        User author = userRepository.findById(blogPostDTO.getAuthorId()).get();
        blogPost.setAuthor(author);

        BlogPost savedBlogPost = blogPostRepository.save(blogPost);
        BlogPostDTO savedBlogPostDTO = modelMapper.map(savedBlogPost, BlogPostDTO.class);
        savedBlogPostDTO.setCategoryId(category.getCategoryId());
        savedBlogPostDTO.setAuthorId(author.getUserId());

        return savedBlogPostDTO;
    }

    @Override
    public PagedResponse<BlogPostDTO> getBlogPosts(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<BlogPost> blogPostPage = blogPostRepository.findAll(pageable);

        List<BlogPostDTO> content = blogPostPage.stream()
                .map(
                        blogPost -> {
                            BlogPostDTO blogPostDTO = modelMapper.map(blogPost, BlogPostDTO.class);
                            blogPostDTO.setCategoryId(blogPost.getCategory().getCategoryId());
                            blogPostDTO.setAuthorId(blogPost.getAuthor().getUserId());
                            return blogPostDTO;
                        }
                ).toList();

        PagedResponse<BlogPostDTO> blogPostDTOPagedResponse = new PagedResponse<>();
        blogPostDTOPagedResponse.setContent(content);
        blogPostDTOPagedResponse.setPageNumber(blogPostPage.getNumber());
        blogPostDTOPagedResponse.setPageSize(blogPostPage.getSize());
        blogPostDTOPagedResponse.setTotalPages(blogPostPage.getTotalPages());
        blogPostDTOPagedResponse.setTotalElements(blogPostPage.getTotalElements());
        blogPostDTOPagedResponse.setIsLastPage(blogPostPage.isLast());

        return blogPostDTOPagedResponse;
    }

    @Override
    public BlogPostDTO getBlogPostById(Long id) {
        BlogPost blogPost = blogPostRepository.findBlogPostByBlogPostId(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("BlogPost", "blogPostId", id)
                );

        BlogPostDTO blogPostDTO = modelMapper.map(blogPost, BlogPostDTO.class);
        blogPostDTO.setCategoryId(blogPost.getCategory().getCategoryId());
        blogPostDTO.setAuthorId(blogPost.getAuthor().getUserId());

        return blogPostDTO;
    }

    @Override
    public BlogPostDTO updateBlogPost(Long id, BlogPostDTO blogPostDTO) {
        if (!userRepository.existsUserByUserId(blogPostDTO.getAuthorId())) {
            throw new ResourceNotFoundException("User", "userId", blogPostDTO.getAuthorId());
        }

        if (!categoryRepository.existsCategoryByCategoryId(blogPostDTO.getCategoryId())) {
            throw new ResourceNotFoundException("Category", "categoryId", blogPostDTO.getCategoryId());
        }

        BlogPost blogPost = blogPostRepository.findBlogPostByBlogPostId(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("BlogPost", "blogPostId", id)
                );

        BlogPost blogPostBeforeUpdate = modelMapper.map(blogPostDTO, BlogPost.class);

        Category category = categoryRepository.findById(blogPostDTO.getCategoryId()).get();
        blogPostBeforeUpdate.setCategory(category);

        User author = userRepository.findById(blogPostDTO.getAuthorId()).get();
        blogPostBeforeUpdate.setAuthor(author);

        BlogPost blogPostAfterUpdate = blogPostRepository.save(blogPostBeforeUpdate);

        BlogPostDTO updatedBlogPostDTO = modelMapper.map(blogPostAfterUpdate, BlogPostDTO.class);
        updatedBlogPostDTO.setCategoryId(category.getCategoryId());
        updatedBlogPostDTO.setAuthorId(author.getUserId());

        return updatedBlogPostDTO;
    }

    @Override
    public BlogPostDTO deleteBlogPost(Long id) {
        BlogPost blogPost = blogPostRepository.findBlogPostByBlogPostId(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("BlogPost", "blogPostId", id)
                );

        blogPostRepository.delete(blogPost);

        BlogPostDTO deletedBlogPostDTO = modelMapper.map(blogPost, BlogPostDTO.class);
        deletedBlogPostDTO.setCategoryId(blogPost.getCategory().getCategoryId());
        deletedBlogPostDTO.setAuthorId(blogPost.getAuthor().getUserId());

        return deletedBlogPostDTO;
    }

    @Override
    public PagedResponse<BlogPostDTO> searchBlogPosts(String keyword, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<BlogPost> blogPostPage = blogPostRepository.findAll(BlogPostSpecification.blogPostConatainsKeyword(keyword), pageable);

        List<BlogPostDTO> content = blogPostPage.stream()
                .map(
                        blogPost -> {
                            BlogPostDTO blogPostDTO = modelMapper.map(blogPost, BlogPostDTO.class);
                            blogPostDTO.setCategoryId(blogPost.getCategory().getCategoryId());
                            blogPostDTO.setAuthorId(blogPost.getAuthor().getUserId());
                            return blogPostDTO;
                        }
                ).toList();

        PagedResponse<BlogPostDTO> blogPostDTOPagedResponse = new PagedResponse<>();
        blogPostDTOPagedResponse.setContent(content);
        blogPostDTOPagedResponse.setPageNumber(blogPostPage.getNumber());
        blogPostDTOPagedResponse.setPageSize(blogPostPage.getSize());
        blogPostDTOPagedResponse.setTotalPages(blogPostPage.getTotalPages());
        blogPostDTOPagedResponse.setTotalElements(blogPostPage.getTotalElements());
        blogPostDTOPagedResponse.setIsLastPage(blogPostPage.isLast());

        return blogPostDTOPagedResponse;
    }

    @Override
    public PagedResponse<BlogPostDTO> getBlogPostsByCategoryName(String categoryName, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        if (categoryName == null || categoryName.isEmpty()) {
            throw new ApiException("Category Name should be specified");
        }

        Category category = categoryRepository.findCategoryByCategoryNameIgnoreCase(categoryName).orElseThrow(
                () -> new ResourceNotFoundException("Category", "categoryName", categoryName)
        );

        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<BlogPost> blogPostPage = blogPostRepository.findBlogPostsByCategory(category, pageable);

        List<BlogPostDTO> content = blogPostPage.stream()
                .map(
                        blogPost -> {
                            BlogPostDTO blogPostDTO = modelMapper.map(blogPost, BlogPostDTO.class);
                            blogPostDTO.setCategoryId(blogPost.getCategory().getCategoryId());
                            blogPostDTO.setAuthorId(blogPost.getAuthor().getUserId());
                            return blogPostDTO;
                        }
                ).toList();

        PagedResponse<BlogPostDTO> blogPostDTOPagedResponse = new PagedResponse<>();
        blogPostDTOPagedResponse.setContent(content);
        blogPostDTOPagedResponse.setPageNumber(blogPostPage.getNumber());
        blogPostDTOPagedResponse.setPageSize(blogPostPage.getSize());
        blogPostDTOPagedResponse.setTotalPages(blogPostPage.getTotalPages());
        blogPostDTOPagedResponse.setTotalElements(blogPostPage.getTotalElements());
        blogPostDTOPagedResponse.setIsLastPage(blogPostPage.isLast());

        return blogPostDTOPagedResponse;
    }

    @Override
    public PagedResponse<BlogPostDTO> getBlogPostsByAuthorName(String authorName, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        if (authorName == null || authorName.isEmpty()) {
            throw new ApiException("Author Name should be specified");
        }

        User author = userRepository.findUserByUsername(authorName).orElseThrow(
                () -> new ResourceNotFoundException("Author", "authorName", authorName)
        );

        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<BlogPost> blogPostPage = blogPostRepository.findBlogPostsByAuthor(author, pageable);

        List<BlogPostDTO> content = blogPostPage.stream()
                .map(
                        blogPost -> {
                            BlogPostDTO blogPostDTO = modelMapper.map(blogPost, BlogPostDTO.class);
                            blogPostDTO.setCategoryId(blogPost.getCategory().getCategoryId());
                            blogPostDTO.setAuthorId(blogPost.getAuthor().getUserId());
                            return blogPostDTO;
                        }
                ).toList();

        PagedResponse<BlogPostDTO> blogPostDTOPagedResponse = new PagedResponse<>();
        blogPostDTOPagedResponse.setContent(content);
        blogPostDTOPagedResponse.setPageNumber(blogPostPage.getNumber());
        blogPostDTOPagedResponse.setPageSize(blogPostPage.getSize());
        blogPostDTOPagedResponse.setTotalPages(blogPostPage.getTotalPages());
        blogPostDTOPagedResponse.setTotalElements(blogPostPage.getTotalElements());
        blogPostDTOPagedResponse.setIsLastPage(blogPostPage.isLast());

        return blogPostDTOPagedResponse;
    }
}
