package com.example.blog.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final int MAX_REQUESTS_PER_MINUTE = 5;
    private final Map<String, RateLimitCounter> counterMap = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        long now = System.currentTimeMillis();
        RateLimitCounter counter = counterMap.compute(ip, (key, old) -> {
            if (old == null || now - old.windowStart > 60_000) {
                return new RateLimitCounter(now);
            }
            return old;
        });
        if (counter.count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(429);
            response.getWriter().write("{\"code\":429,\"message\":\"评论提交过于频繁，请稍后再试\"}");
            return false;
        }
        return true;
    }

    private static class RateLimitCounter {
        final long windowStart;
        final AtomicInteger count = new AtomicInteger(1);

        RateLimitCounter(long windowStart) {
            this.windowStart = windowStart;
        }
    }
}
