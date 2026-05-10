package com.example.blog.config;

import com.example.blog.annotation.RateLimit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Component
public class RateLimitConfig implements HandlerInterceptor {

    private final Map<String, LinkedList<Long>> requestTimestamps = new HashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RateLimit rateLimit = handlerMethod.getMethodAnnotation(RateLimit.class);

        if (rateLimit == null) {
            return true;
        }

        String ip = request.getRemoteAddr();
        String key = ip + ":" + request.getRequestURI();

        int maxRequests = rateLimit.maxRequests();
        int windowSeconds = rateLimit.windowSeconds();

        LinkedList<Long> timestamps = requestTimestamps.computeIfAbsent(key, k -> new LinkedList<>());

        long now = System.currentTimeMillis();
        long windowStart = now - (windowSeconds * 1000L);

        while (!timestamps.isEmpty() && timestamps.getFirst() < windowStart) {
            timestamps.removeFirst();
        }

        if (timestamps.size() >= maxRequests) {
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\": 429, \"message\": \"请求过于频繁，请稍后再试\"}");
            return false;
        }

        timestamps.addLast(now);
        return true;
    }
}
