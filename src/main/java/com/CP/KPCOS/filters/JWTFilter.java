package com.CP.KPCOS.filters;


import com.CP.KPCOS.exceptions.ErrorDetails;
import com.CP.KPCOS.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtil;

    private final RedisTemplate<String, String> redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final List<Pair<String, String>> bypassToken = Arrays.asList(
                Pair.of("/swagger-ui/index.html", "GET"),
                Pair.of("/v3/api-docs", "GET"),
                Pair.of("/swagger-ui/swagger-initializer.js", "GET"),
                Pair.of("/swagger-ui/favicon-16x16.png", "GET"),
                Pair.of("/swagger-ui/favicon-32x32.png", "GET"),
                Pair.of("/swagger-ui/swagger-ui.css", "GET"),
                Pair.of("/swagger-ui/index.css", "GET"),
                Pair.of("/swagger-ui/swagger-ui-bundle.js", "GET"),
                Pair.of("/swagger-ui/swagger-ui-standalone-preset.js", "GET"),
                Pair.of("/api-docs/swagger-config", "GET"),
                Pair.of("/api-docs", "GET"),
                Pair.of("/api/v1/auth/login", "POST"),
                Pair.of("/api/v1/auth/register", "POST"),
                Pair.of("/api/v1/test", "GET")
        );
        for (Pair<String, String> bypasstoken : bypassToken) {
            if (request.getServletPath().equals(bypasstoken.getFirst()) && request.getMethod().equals(bypasstoken.getSecond())) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), "Bad Request", "Token is required", List.of("Token is required"));
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), errorDetails);
            return;
        }
        try {
            jwtUtil.verifyToken(token, false);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.UNAUTHORIZED.value(), request.getRequestURI(), "Unauthorized", "Token is invalid", List.of("Token is invalid"));
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), errorDetails);
            return;
        }


        filterChain.doFilter(request, response);
    }
}
