package com.example.Carrer_backend.Middleware;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class authMiddleware extends OncePerRequestFilter{

    private final String secret;

    public authMiddleware(String secret) {
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        
        if(request.getCookies() != null){
            
            for(Cookie cookie : request.getCookies()){
                if("token".equals(cookie.getName())){
                    token = cookie.getValue();
                }
            }
        }

        if(token == null){
            String authHeader = request.getHeader("Authorization");
            if(authHeader != null && authHeader.startsWith("Bearer ")){
                token = authHeader.substring(7);
            }
        } 

        if(token == null){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
            return;
        }

        try {
            
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            
            Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();

            String userId = claims.getSubject();
            String email = claims.get("email", String.class);

            request.setAttribute("userId", userId);
            request.setAttribute("email", email);
            
            filterChain.doFilter(request, response);

            
            
        } catch (Exception e) {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
            return;
        }

        
    }
    
}
