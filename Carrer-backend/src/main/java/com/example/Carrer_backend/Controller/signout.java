package com.example.Carrer_backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Carrer_backend.Service.signOutUser;

@RestController
@RequestMapping("/api/auth/signout")
public class signout {

    private final signOutUser signOutUser;

    public signout(signOutUser signOutUser) {
        this.signOutUser = signOutUser;
    }

    @PostMapping
    public ResponseEntity<?> signout_user(){

        return signOutUser.logout();
        
    }

    
}
