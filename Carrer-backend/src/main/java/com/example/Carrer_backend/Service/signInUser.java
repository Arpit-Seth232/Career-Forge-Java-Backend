package com.example.Carrer_backend.Service;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
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

@Service
public class signInUser {

    @Value("${jwt.secret}")
    private String secret;
    private final ObjectMapper objectMapper;
    
    private final userRepository userRepository;

    public signInUser(userRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<JsonNode> validateUser(String email, String password) {
        try{
            if(email == null || password == null){
                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "Please provide all the fields");
                return ResponseEntity.badRequest().body(response);
            }

            Optional<user> existed_user = userRepository.findByEmail(email);
        
            if(existed_user.isEmpty()){
                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "User not found");
                return ResponseEntity.badRequest().body(response);
            }

            user user_details = existed_user.get();

            if(!user_details.getPassword().equals(password)){
                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "Invalid password");
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
            .maxAge(Duration.ofHours(10))
            .build();
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.SET_COOKIE, cookie.toString());

            JsonNode response = objectMapper.createObjectNode()
                    .put("isSuccess", "true")
                    .put("message", "User logged in successfully")
                    .set("data", objectMapper.valueToTree(user_details));
            
            return ResponseEntity.ok().headers(header).body(response);
            
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
