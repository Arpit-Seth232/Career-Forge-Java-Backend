package com.example.Carrer_backend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.userRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class updateRole {

    private userRepository userRepository;
    private final ObjectMapper objectMapper;

    public updateRole(userRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    
    public ResponseEntity<JsonNode> updateRole_user(String userId, String role){
        try{
            
            user user_details = userRepository.findById(userId).get();

            user_details.setRole(role);
            userRepository.save(user_details);

            JsonNode response = objectMapper.createObjectNode()
                    .put("isSuccess", "true")
                    .put("message", "User role updated successfully")
                    .set("data", objectMapper.valueToTree(user_details));
            
            return ResponseEntity.ok().body(response);
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
