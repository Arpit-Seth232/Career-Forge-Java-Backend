package com.example.Carrer_backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Carrer_backend.DTO.updateRecruiterProfileRequest;
import com.example.Carrer_backend.Service.createRecruiterProfile;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

@RestController
@RequestMapping("/api/auth/recruiter/profile")
public class setRecruiterProfile {

    private final createRecruiterProfile createRecruiterProfile;

    public setRecruiterProfile(createRecruiterProfile createRecruiterProfile) {
        this.createRecruiterProfile = createRecruiterProfile;
    }

    @PostMapping
    public ResponseEntity<?> update_recruiter_profile(@RequestBody
        updateRecruiterProfileRequest updateRecruiterProfileRequest,
        HttpServletRequest request
    ){
        
        try{

            String userId = request.getAttribute("userId").toString();
            String fullName = updateRecruiterProfileRequest.getFullName();
            String companyName = updateRecruiterProfileRequest.getCompanyName();
            String profilePicture = updateRecruiterProfileRequest.getProfilePicture();
            String jobTitle = updateRecruiterProfileRequest.getJobTitle();
            String companySize = updateRecruiterProfileRequest.getCompanySize();
            String industry = updateRecruiterProfileRequest.getIndustry();


        return createRecruiterProfile.create_recruiter_profile(userId, fullName, companyName, profilePicture, jobTitle, companySize, industry);
            
            
        }
        catch(Exception e){
            Map<String, String> response = new HashMap<>();
            response.put("isSuccess", "false");
            response.put("message", "Error updating profile completion status " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

    }
    
}
