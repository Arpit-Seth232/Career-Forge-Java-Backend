package com.example.Carrer_backend.Service;



import java.util.*;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.userRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    private final ObjectMapper objectMapper;

    public createUser(userRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<JsonNode> user_creation(String name, String email, String password, String confirmPassword) {
        
        try{
        if (name == null || email == null || password == null || confirmPassword == null) {
            JsonNode response = objectMapper.createObjectNode()
                    .put("isSuccess", "false")
                    .put("message", "Please provide all the fields");
            return ResponseEntity.badRequest().body(response);
        }

        if (!password.equals(confirmPassword)) {
            JsonNode response = objectMapper.createObjectNode()
                    .put("isSuccess", "false")
                    .put("message", "Password and confirm password do not match");
            return ResponseEntity.badRequest().body(response);
        }

        if (userRepository.existsByEmail(email)) {
            JsonNode response = objectMapper.createObjectNode()
                    .put("isSuccess", "false")
                    .put("message", "Email already exists");
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

        JsonNode createdUserInfo = objectMapper.createObjectNode()
                .put("name", saved_user.getName())
                .put("role", saved_user.getRole())
                .put("email", saved_user.getEmail())
                .put("isProfileCompleted", saved_user.getIsProfileCompleted());

        JsonNode response = objectMapper.createObjectNode()
                .put("isSuccess", "true")
                .put("message", "User created successfully")
                .set("data", createdUserInfo);

        return ResponseEntity.created(null).headers(header).body(response);
    }
    catch(Exception e){
        JsonNode response = objectMapper.createObjectNode()
                .put("isSuccess", "false")
                .put("message", "Internal Server Error")
                .put("error",e.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }
    }

}

