package com.pi.dorossai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class RateLimitConfig {
    
    /**
     * Simple in-memory rate limiter for demonstration purposes.
     * In production, consider using Redis-based solution for distributed systems.
     */
    @Bean
    public RateLimiter rateLimiter() {
        return new RateLimiter();
    }
    
    public static class RateLimiter {
        private final ConcurrentHashMap<String, UserRequestCounter> requestCounts = new ConcurrentHashMap<>();
        private final long windowSizeMs = 60_000; // 1 minute window
        
        public boolean isAllowed(String userId, int maxRequests) {
            long currentTime = System.currentTimeMillis();
            UserRequestCounter counter = requestCounts.computeIfAbsent(userId, k -> new UserRequestCounter());
            
            // Reset counter if window has passed
            if (currentTime - counter.windowStart.get() > windowSizeMs) {
                counter.windowStart.set(currentTime);
                counter.requestCount.set(0);
            }
            
            return counter.requestCount.incrementAndGet() <= maxRequests;
        }
        
        private static class UserRequestCounter {
            final AtomicLong windowStart = new AtomicLong(System.currentTimeMillis());
            final AtomicInteger requestCount = new AtomicInteger(0);
        }
    }
}
