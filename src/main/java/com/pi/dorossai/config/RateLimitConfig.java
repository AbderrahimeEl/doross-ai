package com.pi.dorossai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class RateLimitConfig {
    
    /**
     * Rate limiting has been disabled.
     * This bean is kept for compatibility with existing code.
     */
    @Bean
    public RateLimiter rateLimiter() {
        return new RateLimiter();
    }
    
    public static class RateLimiter {
        private final ConcurrentHashMap<String, UserRequestCounter> requestCounts = new ConcurrentHashMap<>();
        private final long windowSizeMs = 60_000; // 1 minute window
        
        public boolean isAllowed(String userId, int maxRequests) {
            // Rate limiting disabled - always return true
            return true;
        }
        
        private static class UserRequestCounter {
            final AtomicLong windowStart = new AtomicLong(System.currentTimeMillis());
            final AtomicInteger requestCount = new AtomicInteger(0);
        }
    }
}
