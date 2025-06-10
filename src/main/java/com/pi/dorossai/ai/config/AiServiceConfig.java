package com.pi.dorossai.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AiServiceConfig {
    
    @Value("${dorossai.ai.openai.api-key}")
    private String openaiApiKey;
    
    @Value("${dorossai.ai.github.api-key}")
    private String githubApiKey;
    
    @Value("${dorossai.ai.timeout.connection:5000}")
    private int connectionTimeout;
    
    @Value("${dorossai.ai.timeout.read:30000}")
    private int readTimeout;
    
    public String getOpenaiApiKey() {
        return openaiApiKey;
    }
    
    public String getGithubApiKey() {
        return githubApiKey;
    }
    
    @Bean(name = "aiRestTemplate")
    public RestTemplate aiRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectionTimeout);
        factory.setReadTimeout(readTimeout);
        return new RestTemplate(factory);
    }
}
