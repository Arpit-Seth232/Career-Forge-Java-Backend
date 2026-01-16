package com.example.Carrer_backend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Carrer_backend.Entity.recruiter;
import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.recruiterRepository;
import com.example.Carrer_backend.Repository.userRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class createRecruiterProfile {

    private final userRepository userRepository;
    private final recruiterRepository recruiterRepository;
    private final ObjectMapper objectMapper;

    public createRecruiterProfile(userRepository userRepository, recruiterRepository recruiterRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.recruiterRepository = recruiterRepository;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<JsonNode> create_recruiter_profile(String userId, String fullName, String companyName,
            String profilePicture, String jobTitle, String companySize, String industry) {

        try {

            if (fullName == null || companyName == null || jobTitle == null || companySize == null
                    || industry == null) {

                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "All fields are required");
                return ResponseEntity.badRequest().body(response);
            }

            if (userId == null) {
                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "User ID not found");
                return ResponseEntity.badRequest().body(response);
            }

            Optional<recruiter> userDetails = recruiterRepository.findById(userId);

            if (userDetails.isPresent()) {

                recruiter existingUser = userDetails.get();

                existingUser.setFullName(fullName);
                if (profilePicture != null)
                    existingUser.setProfilePicture(profilePicture);
                existingUser.setJobTitle(jobTitle);
                existingUser.setCompanySize(companySize);
                existingUser.setIndustry(industry);
                existingUser.setCompanyName(companyName);

                user alreadyExistingUser = userRepository.findById(userId).get();

                alreadyExistingUser.setIsProfileCompleted(true);

                recruiter savedUser = recruiterRepository.save(existingUser);
                userRepository.save(alreadyExistingUser);

                JsonNode createdUserInfo = objectMapper.createObjectNode()
                        .put("name", savedUser.getFullName())
                        .put("role", alreadyExistingUser.getRole())
                        .put("email", alreadyExistingUser.getEmail())
                        .put("isProfileCompleted", alreadyExistingUser.getIsProfileCompleted())
                        .put("profilePicture", savedUser.getProfilePicture())
                        .put("jobTitle", savedUser.getJobTitle())
                        .put("companySize", savedUser.getCompanySize())
                        .put("industry", savedUser.getIndustry())
                        .put("companyName", savedUser.getCompanyName());

                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "true")
                        .put("message", "Profile completion status updated successfully")
                        .set("data", createdUserInfo);
                return ResponseEntity.ok().body(response);
            }

            else {

                recruiter newUser = new recruiter();

                user alreadyExistingUser = userRepository.findById(userId).get();

                newUser.setPersonalDetails(alreadyExistingUser);
                newUser.setFullName(fullName);
                if (profilePicture != null)
                    newUser.setProfilePicture(profilePicture);
                newUser.setJobTitle(jobTitle);
                newUser.setCompanySize(companySize);
                newUser.setIndustry(industry);
                newUser.setCompanyName(companyName);

                alreadyExistingUser.setIsProfileCompleted(true);

                recruiter savedUser = recruiterRepository.save(newUser);
                userRepository.save(alreadyExistingUser);

                JsonNode createdUserInfo = objectMapper.createObjectNode()
                        .put("name", savedUser.getFullName())
                        .put("role", alreadyExistingUser.getRole())
                        .put("email", alreadyExistingUser.getEmail())
                        .put("isProfileCompleted", alreadyExistingUser.getIsProfileCompleted())
                        .put("profilePicture", savedUser.getProfilePicture())
                        .put("jobTitle", savedUser.getJobTitle())
                        .put("companySize", savedUser.getCompanySize())
                        .put("industry", savedUser.getIndustry())
                        .put("companyName", savedUser.getCompanyName());

                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "true")
                        .put("message", "Profile completion status updated successfully")
                        .set("data", createdUserInfo);
                return ResponseEntity.ok().body(response);
            }

        } catch (Exception e) {
            JsonNode response = objectMapper.createObjectNode()
                    .put("isSuccess", "false")
                    .put("message", "Error updating profile completion status ")
                    .put("error",e.getMessage());
            return ResponseEntity.internalServerError().body(response);

        }
    }
}
