package com.example.Carrer_backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Carrer_backend.Service.fetchProfile;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth/me")
public class me {

    private final fetchProfile fetchProfile;
    
    public me(fetchProfile fetchProfile) {
        this.fetchProfile = fetchProfile;
    }
    
    @GetMapping
    public ResponseEntity<?> getMe(HttpServletRequest request){

        String userId = request.getAttribute("userId").toString();

        return fetchProfile.getProfile(userId);
        
    }
    
}
