package com.strawhats.blogplatform.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostResponse {

    private Long id;
    private String title;
    private String content;
    private String categoryName;
    private String authorUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}