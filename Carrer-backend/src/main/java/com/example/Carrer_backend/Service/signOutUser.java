package com.example.Carrer_backend.Service;




import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class signOutUser {

    private final ObjectMapper objectMapper;

    public signOutUser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<JsonNode> logout(){
        
        try{
        // clear the cookie
        ResponseCookie delete_cookie = ResponseCookie.from("token", "")
        .path("/")
        .httpOnly(true)
        .sameSite("Lax")
        .maxAge(0)
        .build();

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.SET_COOKIE, delete_cookie.toString());

        JsonNode response = objectMapper.createObjectNode()
                .put("isSuccess", "true")
                .put("message", "User logged out successfully");


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
