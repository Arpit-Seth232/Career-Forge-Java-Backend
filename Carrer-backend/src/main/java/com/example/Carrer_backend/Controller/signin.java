package com.example.Carrer_backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Carrer_backend.DTO.signInRequest;
import com.example.Carrer_backend.Service.signInUser;

@RestController
@RequestMapping("/api/signin")
public class signin {

    private final signInUser signInUser;
    
    public signin(signInUser signInUser) {
        this.signInUser = signInUser;
    }
    
    @PostMapping
    public ResponseEntity<?> signin(@RequestBody signInRequest logInRequest){

        String email = logInRequest.getEmail();
        String password = logInRequest.getPassword();

        return signInUser.validateUser(email, password);

    }
}
