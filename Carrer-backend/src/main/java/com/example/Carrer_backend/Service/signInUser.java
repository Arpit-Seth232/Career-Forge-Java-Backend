package com.example.Carrer_backend.Service;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.userRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class signInUser {

    @Value("${jwt.secret}")
    private String secret;
    
    private final userRepository userRepository;

    public signInUser(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> validateUser(String email, String password) {
        try{
            if(email == null || password == null){
                Map<String, String> response = new HashMap<>();
                response.put("isSuccess", "false");
                response.put("message", "Please provide all the fields");
                return ResponseEntity.badRequest().body(response);
            }

            Optional<user> existed_user = userRepository.findByEmail(email);
        
            if(existed_user.isEmpty()){
                Map<String, String> response = new HashMap<>();
                response.put("isSuccess", "false");
                response.put("message", "User not found");
                return ResponseEntity.badRequest().body(response);
            }

            user user_details = existed_user.get();

            if(!user_details.getPassword().equals(password)){
                Map<String, String> response = new HashMap<>();
                response.put("isSuccess", "false");
                response.put("message", "Invalid password");
                return ResponseEntity.badRequest().body(response);
            }

            Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            String token = Jwts.builder()
            .subject(user_details.getId())
            .claim("email", user_details.getEmail())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(key)
            .compact();

            ResponseCookie cookie = ResponseCookie.from("token", token)
            .path("/")
            .httpOnly(true)
            .sameSite("Lax")
            .maxAge(1000 * 60 * 60 * 10)
            .build();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            Map<String, Object> response = new HashMap<>();
            response.put("isSuccess", "true");
            response.put("message", "User logged in successfully");
            response.put("data", user_details);
            
            return ResponseEntity.ok().body(response);
            
        }
        catch(Exception e){
            Map<String, String> response = new HashMap<>();
            response.put("isSuccess", "false");
            response.put("message", "Internal Server Error");
            response.put("error",e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
}
