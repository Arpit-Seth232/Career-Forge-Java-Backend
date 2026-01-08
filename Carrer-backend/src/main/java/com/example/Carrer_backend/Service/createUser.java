package com.example.Carrer_backend.Service;



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
import java.security.Key;
import java.time.Duration;
import java.nio.charset.StandardCharsets;

@Service
public class createUser {

    private final userRepository userRepository;
    @Value("${jwt.secret}")
    private String secret;

    public createUser(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> user_creation(String name, String email, String password, String confirmPassword) {
        
        try{
        if (name == null || email == null || password == null || confirmPassword == null) {
            Map<String, String> response = new HashMap<>();
            response.put("isSuccess", "false");
            response.put("message", "Please provide all the fields");
            return ResponseEntity.badRequest().body(response);
        }

        if (!password.equals(confirmPassword)) {
            Map<String, String> response = new HashMap<>();
            response.put("isSuccess", "false");
            response.put("message", "Password and confirm password do not match");
            return ResponseEntity.badRequest().body(response);
        }

        if (userRepository.existsByEmail(email)) {
            Map<String, String> response = new HashMap<>();
            response.put("isSuccess", "false");
            response.put("message", "Email already exists");
            return ResponseEntity.badRequest().body(response);
        }

        user new_user = new user();

        String userId = UUID.randomUUID().toString();
        new_user.setId(userId);
        new_user.setName(name);
        new_user.setEmail(email);
        new_user.setPassword(password);
        

        
        
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        
        String token = Jwts.builder()
        .subject(userId)
        .claim("email", email)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
        .signWith(key)
        .compact();
        
        ResponseCookie cookie = ResponseCookie.from("token", token)
        .path("/")
        .httpOnly(true)
        .sameSite("Lax")
        .maxAge(Duration.ofHours(10))
        .build();
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.SET_COOKIE, cookie.toString());
        
        user saved_user = userRepository.save(new_user);

        Map<String,Object> response = new HashMap<>();
        response.put("isSuccess", "true");
        response.put("message", "User created successfully");
        response.put("data",saved_user);

        return ResponseEntity.created(null).headers(header).body(response);
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

