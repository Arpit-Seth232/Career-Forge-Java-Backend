package com.example.Carrer_backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Carrer_backend.Service.suggestRoleService;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth/suggestRoles")
public class suggestRoles {

    private final suggestRoleService suggestRoleService;
    
    public suggestRoles(suggestRoleService suggestRoleService) {
        this.suggestRoleService = suggestRoleService;
    }
    
    @GetMapping
    public ResponseEntity<JsonNode> fetchJobs(HttpServletRequest request){

        String userId = request.getAttribute("userId").toString();

        return suggestRoleService.getRoles(userId);
        
    }
    
}
