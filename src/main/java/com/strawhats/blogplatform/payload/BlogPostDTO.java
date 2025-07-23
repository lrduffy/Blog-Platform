package com.strawhats.blogplatform.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostDTO {

    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private Long authorId;
}
