package com.pi.dorossai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
public class FastApiConfig {
    
    @Value("${fastapi.base.url:http://localhost:8000}")
    private String fastApiBaseUrl;
    
    public String getFastApiBaseUrl() {
        return fastApiBaseUrl;
    }
    
    @Bean
    @Primary
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 sec
        factory.setReadTimeout(30000);   // 30 sec
        return new RestTemplate(factory);
    }
}