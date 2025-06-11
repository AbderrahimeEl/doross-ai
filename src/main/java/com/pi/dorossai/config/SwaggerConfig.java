package com.pi.dorossai.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("DorossAI - AI-Powered Educational Tool API")
                    .version("v1.0")
                    .description("""
                        DorossAI is a comprehensive AI-powered educational platform that provides various learning tools and content generation services.
                        
                        ## Features
                        - **Flashcard Generation**: Create educational flashcards on any topic
                        - **Quiz Creation**: Generate multiple-choice quizzes with explanations
                        - **Text Summarization**: Summarize long texts while preserving key information
                        - **Key Points Extraction**: Extract important points from text content
                        - **Code Explanation**: Analyze and explain code in multiple programming languages
                        - **Writing Improvement**: Enhance text quality and style
                        - **Document Q&A**: Answer questions based on document context
                        - **Content Moderation**: Analyze text for harmful or inappropriate content
                        - **User Management**: Complete user authentication and management system
                        
                        ## Authentication
                        Most endpoints require authentication using JWT tokens. To get started:
                        1. Register a new account using `/api/auth/signup`
                        2. Login with your credentials using `/api/auth/login`
                        3. Use the returned JWT token in the Authorization header: `Bearer <token>`
                        
                        ## Rate Limiting
                        API requests are rate-limited to ensure fair usage. Please refer to response headers for rate limit information.
                        
                        ## Support
                        For API support and documentation, please contact the development team.
                        """)
                    .contact(new io.swagger.v3.oas.models.info.Contact()
                        .name("DorossAI Development Team")
                        .email("support@dorossai.com"))
                    .license(new io.swagger.v3.oas.models.info.License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                    .addSecuritySchemes("BearerAuth", 
                        new io.swagger.v3.oas.models.security.SecurityScheme()
                            .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("JWT token obtained from login endpoint")));
    }
}
