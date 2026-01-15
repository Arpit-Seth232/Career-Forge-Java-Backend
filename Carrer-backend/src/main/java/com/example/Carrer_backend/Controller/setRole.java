package com.example.Carrer_backend.Controller;



import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Carrer_backend.DTO.updateRoleRequest;
import com.example.Carrer_backend.Service.updateRole;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth/role")
public class setRole {

    private updateRole updateRole;
    private final ObjectMapper objectMapper;

    public setRole(updateRole updateRole, ObjectMapper objectMapper) {
        this.updateRole = updateRole;
        this.objectMapper = objectMapper;
    }

    // @GetMapping
    // public ResponseEntity<?> getRole_user(HttpServletRequest request){
    //     try{
    //     String userId = (String) request.getAttribute("userId");
    //     String email = (String) request.getAttribute("email");

    //     Map<String, String> response = new HashMap<>();
    //     response.put("userId", userId);
    //     response.put("email", email);

    //     return ResponseEntity.ok().body(response);
    //     }
    //     catch(Exception e){
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
        
    // }

    @PostMapping
    public ResponseEntity<JsonNode> setRole_user(HttpServletRequest request, @RequestBody updateRoleRequest updateRoleRequest){
        
            String role = updateRoleRequest.getRole();
            if(role == null){
                JsonNode response = objectMapper.createObjectNode()
                .put("isSuccess", "false")
                .put("message", "Please select a role");
                return ResponseEntity.badRequest().body(response);
            }

            String userId = (String) request.getAttribute("userId");
            
            // String email = (String) request.getAttribute("email");

            if(userId == null){
                JsonNode response = objectMapper.createObjectNode()
                .put("isSuccess", "false")
                .put("message", "User ID not found");
                return ResponseEntity.badRequest().body(response);
            }


            
        try{
            return  updateRole.updateRole_user(userId, role);
        }
        catch(Exception e){
            JsonNode response = objectMapper.createObjectNode()
            .put("isSuccess", "false")
            .put("message", "Internal Server Error");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
}

