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
    private final apiLogService apiLogService;

    public updateRole(userRepository userRepository, ObjectMapper objectMapper, apiLogService apiLogService) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.apiLogService = apiLogService;
    }

    
    public ResponseEntity<JsonNode> updateRole_user(String userId, String role){
        try{
            
            user user_details = userRepository.findById(userId).get();

            user_details.setRole(role);
            user updated_user = userRepository.save(user_details);

            JsonNode updated_user_info = objectMapper.createObjectNode()
                    .put("name", updated_user.getName())
                    .put("role", updated_user.getRole())
                    .put("email", updated_user.getEmail())
                    .put("isProfileCompleted", updated_user.getIsProfileCompleted());


            JsonNode response = objectMapper.createObjectNode()
                    .put("isSuccess", "true")
                    .put("message", "User role updated successfully")
                    .set("data", updated_user_info);

            apiLogService.logApiHit(userId, "api/auth/role", "User role updated successfully");
            
            return ResponseEntity.ok().body(response);
        }
        catch(Exception e){
            JsonNode response = objectMapper.createObjectNode()
                    .put("isSuccess", "false")
                    .put("message", "Internal Server Error")
                    .put("error",e.getMessage());

            apiLogService.logApiHit(userId, "api/auth/role", "Internal Server Error due to " + e.getMessage());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
