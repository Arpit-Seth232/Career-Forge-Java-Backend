package com.example.Carrer_backend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.userRepository;

@Service
public class updateRole {

    private userRepository userRepository;

    public updateRole(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    public ResponseEntity<?> updateRole_user(String userId, String role){
        try{
            
            user user_details = userRepository.findById(userId).get();

            user_details.setRole(role);
            userRepository.save(user_details);

            Map<String, Object> response = new HashMap<>();
            response.put("isSuccess", "true");
            response.put("message", "User role updated successfully");
            response.put("data", user_details);
            
            return ResponseEntity.ok().body(response);
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
