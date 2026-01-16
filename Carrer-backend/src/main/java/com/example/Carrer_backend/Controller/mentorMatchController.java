package com.example.Carrer_backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Carrer_backend.Service.mentorMatchService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth/mentorMatch")
public class mentorMatchController {
    
    private final mentorMatchService mentorMatchService;

    public mentorMatchController(mentorMatchService mentorMatchService) {
        this.mentorMatchService = mentorMatchService;
    }
    
    @GetMapping
    public ResponseEntity<JsonNode> getMentorMatch(HttpServletRequest request){


        String userId = request.getAttribute("userId").toString();

        return mentorMatchService.fetchMentors(userId);
        
    }
}
