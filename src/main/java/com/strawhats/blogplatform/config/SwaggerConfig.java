package com.strawhats.blogplatform.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Blog Platform")
                                .description("""
                                        A Blog Platform built with Spring Boot that allows users to create, read, update, delete, 
                                        and search blog posts. It supports features like pagination, sorting, categorization, and 
                                        author-based filtering. Built using the Spring Boot framework, it provides a robust backend 
                                        with RESTful APIs, integrating with databases and ensuring scalable, secure, and maintainable 
                                        code architecture.
                                        """)
                                .version("1.0")
                );
    }

}
