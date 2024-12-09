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
import org.springframework.lang.NonNull;
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
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final List<Pair<String, String>> bypassToken = Arrays.asList(
                Pair.of("/swagger-ui/index.html", "GET"),
                Pair.of("/swagger-ui", "GET"),
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
        for (Pair<String, String> bypass : bypassToken) {
            if (request.getServletPath().equals(bypass.getFirst()) && request.getMethod().equals(bypass.getSecond())) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            sendErrorResponse(response, HttpStatus.BAD_REQUEST, request.getRequestURI(), "Token is required");
            return;
        }

        try {
            jwtUtil.verifyToken(token, false);
        } catch (Exception e) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, request.getRequestURI(), "Token is invalid");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String path, String message) throws IOException {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), status.value(), path, status.getReasonPhrase(), message, List.of(message));
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(status.value());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), errorDetails);
    }
}
