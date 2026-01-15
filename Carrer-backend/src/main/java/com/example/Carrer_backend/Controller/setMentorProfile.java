package com.example.Carrer_backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Carrer_backend.DTO.updateMentorProfileRequest;
import com.example.Carrer_backend.Service.createMentorProfile;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

@RestController
@RequestMapping("/api/auth/mentor/profile")
public class setMentorProfile {

    private final createMentorProfile createMentorProfile;

    public setMentorProfile(createMentorProfile createMentorProfile) {
        this.createMentorProfile = createMentorProfile;
    }

    @PostMapping
    public ResponseEntity<JsonNode> updateMentorProfile(@RequestBody updateMentorProfileRequest updateMentorProfileRequest, HttpServletRequest request){

        String userId = (String) request.getAttribute("userId");

        String fullName = updateMentorProfileRequest.getFullName();
        String profilePicture = updateMentorProfileRequest.getProfilePicture();
        String yearOfMentoring = updateMentorProfileRequest.getYearOfMentoring();
        String bio = updateMentorProfileRequest.getBio();
        List<String> expertise = updateMentorProfileRequest.getExpertise();

        return createMentorProfile.update_mentor_profile(fullName, profilePicture, yearOfMentoring, bio, expertise, userId);

        
    }
    
}
