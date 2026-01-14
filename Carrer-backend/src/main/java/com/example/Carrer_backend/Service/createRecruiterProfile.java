package com.example.Carrer_backend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Carrer_backend.Entity.recruiter;
import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.recruiterRepository;
import com.example.Carrer_backend.Repository.userRepository;

import java.util.*;

@Service
public class createRecruiterProfile {

    private final userRepository userRepository;
    private final recruiterRepository recruiterRepository;

    public createRecruiterProfile(userRepository userRepository, recruiterRepository recruiterRepository) {
        this.userRepository = userRepository;
        this.recruiterRepository = recruiterRepository;
    }

    public ResponseEntity<?> create_recruiter_profile(String userId, String fullName, String companyName,
            String profilePicture, String jobTitle, String companySize, String industry) {

        try {

            if (fullName == null || companyName == null || jobTitle == null || companySize == null
                    || industry == null) {

                Map<String, String> response = new HashMap<>();
                response.put("isSuccess", "false");
                response.put("message", "All fields are required");
                return ResponseEntity.badRequest().body(response);
            }

            if (userId == null) {
                Map<String, String> response = new HashMap<>();
                response.put("isSuccess", "false");
                response.put("message", "User ID not found");
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

                Map<String, Object> response = new HashMap<>();
                response.put("isSuccess", "true");
                response.put("message", "Profile completion status updated successfully");
                response.put("data", savedUser);
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

                Map<String, Object> response = new HashMap<>();
                response.put("isSuccess", "true");
                response.put("message", "Profile completion status updated successfully");
                response.put("data", savedUser);
                return ResponseEntity.ok().body(response);
            }

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("isSuccess", "false");
            response.put("message", "Error updating profile completion status " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);

        }
    }
}
