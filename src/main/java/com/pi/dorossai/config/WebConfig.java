package com.pi.dorossai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    // Rate limiting has been removed
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // No interceptors added - rate limiting removed
    }
}
