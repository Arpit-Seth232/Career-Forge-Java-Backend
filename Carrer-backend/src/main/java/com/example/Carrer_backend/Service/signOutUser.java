package com.example.Carrer_backend.Service;



import java.util.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class signOutUser {

    public ResponseEntity<?> logout(){
        
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

        Map<String, String> response = new HashMap<>();
        response.put("isSuccess", "true");
        response.put("message", "User logged out successfully");


        return ResponseEntity.ok().headers(header).body(response);

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
