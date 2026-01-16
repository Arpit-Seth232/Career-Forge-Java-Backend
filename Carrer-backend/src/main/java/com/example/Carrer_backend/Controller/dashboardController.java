package com.example.Carrer_backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.Carrer_backend.Service.dashBoardService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth/dashboard")
public class dashboardController {

    private final dashBoardService dashboardService;
    
    public dashboardController(dashBoardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    
    @GetMapping
    public ResponseEntity<JsonNode> getDashboard(HttpServletRequest request) {
        
        String userId = request.getAttribute("userId").toString();

        return dashboardService.getDash_board(userId);
        
        
    }
    
    
    
    
}
