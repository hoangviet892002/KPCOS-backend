package com.CP.KPCOS.filters;


import com.CP.KPCOS.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtil;

    private final RedisTemplate<String, String> redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final List<Pair<String, String>> bypassToken = Arrays.asList(
                new Pair<>("POST", "/api/v1/auth/login"),
                new Pair<>("POST", "/api/v1/auth/register"),
                new Pair<>("POST", "/api/v1/auth/refresh"),
                new Pair<>("GET", "/api/v1/auth/verify")
        );
//        for (Pair<String, String> bypasstoken : bypassToken) {
//            if (request.getServletPath().contains(bypasstoken.getFirst())) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//        }
    }
}
