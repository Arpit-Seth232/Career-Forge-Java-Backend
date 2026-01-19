package com.example.Carrer_backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Carrer_backend.Service.signOutUser;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth/signout")
public class signout {

    private final signOutUser signOutUser;

    public signout(signOutUser signOutUser) {
        this.signOutUser = signOutUser;
    }

    @PostMapping
    public ResponseEntity<JsonNode> signout_user(HttpServletRequest request){

        String userId = request.getAttribute("userId").toString();

        return signOutUser.logout(userId);
        
    }

    
}
