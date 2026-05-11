package com.example.blog.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final int MAX_REQUESTS_PER_MINUTE = 5;
    private final Map<String, RateLimitInfo> rateLimitMap = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = getClientIp(request);
        String key = "comment:" + ip;

        long now = System.currentTimeMillis();
        RateLimitInfo info = rateLimitMap.get(key);

        if (info == null) {
            rateLimitMap.put(key, new RateLimitInfo(now, new AtomicInteger(1)));
            return true;
        }

        long windowStart = now - 60000;
        if (info.getTimestamp() < windowStart) {
            info.setTimestamp(now);
            info.getCount().set(1);
            return true;
        }

        int count = info.getCount().incrementAndGet();
        if (count > MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":429,\"message\":\"评论提交过于频繁，请稍后再试\",\"data\":null}");
            return false;
        }

        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private static class RateLimitInfo {
        private long timestamp;
        private AtomicInteger count;

        public RateLimitInfo(long timestamp, AtomicInteger count) {
            this.timestamp = timestamp;
            this.count = count;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public AtomicInteger getCount() {
            return count;
        }
    }
}
