package com.example.Carrer_backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Carrer_backend.DTO.updateJobseekerProfileRequest;
import com.example.Carrer_backend.Service.createJobseekerProfile;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

@RestController
@RequestMapping("/api/auth/jobseeker/profile")
public class setJobseekerProfile {

    private final createJobseekerProfile createJobseekerProfile;

    public setJobseekerProfile(createJobseekerProfile createJobseekerProfile) {
        this.createJobseekerProfile = createJobseekerProfile;
    }

    @PostMapping
    public ResponseEntity<JsonNode> updateJobseekerProfile(@RequestBody updateJobseekerProfileRequest updateJobseekerProfileRequest, HttpServletRequest request){

            String fullName = updateJobseekerProfileRequest.getFullName();
            String profilePicture = updateJobseekerProfileRequest.getProfilePicture();
            String yearOfExperience = updateJobseekerProfileRequest.getYearOfExperience();
            String bio = updateJobseekerProfileRequest.getBio();
            List<String> preferredRoles = updateJobseekerProfileRequest.getPreferredRoles();
            
            String userId = (String) request.getAttribute("userId");

            // String email = (String) request.getAttribute("email");

            return createJobseekerProfile.update_jobseeker_profile(fullName, profilePicture, yearOfExperience, bio, preferredRoles, userId);

        
        
    }
}
