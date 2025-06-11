package com.pi.dorossai.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {
    
    private final RateLimitConfig.RateLimiter rateLimiter;
      @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        
        // Apply rate limiting to AI endpoints
        if (isAiEndpoint(requestURI)) {
            String userId = getCurrentUserId();
            int maxRequests = getMaxRequestsForEndpoint(requestURI);
            
            if (!rateLimiter.isAllowed(userId, maxRequests)) {
                log.warn("Rate limit exceeded for user {} on endpoint {}", userId, requestURI);
                response.setStatus(429); // Too Many Requests
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Rate limit exceeded. Please try again later.\"}");
                return false;
            }
        }
        
        return true;
    }
      private boolean isAiEndpoint(String uri) {
        return uri.startsWith("/api/") && (
            uri.contains("/summarize") ||
            uri.contains("/generate-quiz") ||
            uri.contains("/extract-key-points") ||
            uri.contains("/generate-flashcards") ||
            uri.contains("/explain-code") ||
            uri.contains("/improve-writing") ||
            uri.contains("/ask-document") ||
            uri.contains("/moderate-content")
        );
    }
    
    private String getCurrentUserId() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            return "anonymous";
        }
    }
    
    private int getMaxRequestsForEndpoint(String uri) {
        // Different rate limits for different endpoints
        if (uri.contains("/generate-quiz")) {
            return 5; // 5 requests per minute for quiz generation
        } else if (uri.contains("/summarize")) {
            return 10; // 10 requests per minute for summarization
        } else {
            return 15; // 15 requests per minute for other AI services
        }
    }
}
