package com.example.Carrer_backend.Controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Carrer_backend.DTO.createUserRequest;

import com.example.Carrer_backend.Service.createUser;

@RestController
@RequestMapping("/api/signup")
public class signup {



    private createUser createUser;

    public signup(createUser createUser) {
        
        this.createUser = createUser;
    }

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody createUserRequest userDetails) {

        
        String name = userDetails.getName();
        String email = userDetails.getEmail();
        String password = userDetails.getPassword();
        String confirmPassword = userDetails.getConfirmPassword();
        
        return createUser.user_creation(name, email, password, confirmPassword);
        
        
        
        
    }
    
    

    
}
