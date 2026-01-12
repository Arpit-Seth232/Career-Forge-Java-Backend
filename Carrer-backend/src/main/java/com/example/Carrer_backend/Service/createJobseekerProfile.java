package com.example.Carrer_backend.Service;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Carrer_backend.Entity.jobseeker;
import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.jobseekerRepository;
import com.example.Carrer_backend.Repository.userRepository;

@Service
public class createJobseekerProfile {

    private final jobseekerRepository jobseekerRepository;
    private final userRepository userRepository;

    public createJobseekerProfile(jobseekerRepository jobseekerRepository, userRepository userRepository) {
        this.jobseekerRepository = jobseekerRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> update_jobseeker_profile(String fullName, String profilePicture, String yearOfExperience,
            String bio, List<String> preferredRoles, String userId) {

        try {

            if (fullName == null || yearOfExperience == null || preferredRoles == null) {
                Map<String, String> response = new HashMap<>();
                response.put("isSuccess", "false");
                response.put("message", "Please provide all the required fields");
                return ResponseEntity.badRequest().body(response);
            }

            if (userId == null) {
                Map<String, String> response = new HashMap<>();
                response.put("isSuccess", "false");
                response.put("message", "User ID not found");
                return ResponseEntity.badRequest().body(response);
            }

            if (preferredRoles.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("isSuccess", "false");
                response.put("message", "Please provide at least one preferred role");
                return ResponseEntity.badRequest().body(response);
            }

            // System.out.println("user id: " + userId);
            Optional<jobseeker> userDetails = jobseekerRepository.findById(userId);

            // System.out.println(preferredRoles);

            if (userDetails.isPresent()) {

                jobseeker existingUser = userDetails.get();

                existingUser.setFullName(fullName);
                if (profilePicture != null)
                    existingUser.setProfilePicture(profilePicture);
                existingUser.setYearOfExperience(yearOfExperience);
                if (bio != null)
                    existingUser.setBio(bio);
                existingUser.setPreferredRoles(preferredRoles);

                user alreadyExistingUser = userRepository.findById(userId).get();

                alreadyExistingUser.setIsProfileCompleted(true);

                jobseeker savedUser = jobseekerRepository.save(existingUser);
                userRepository.save(alreadyExistingUser);

                Map<String, Object> response = new HashMap<>();
                response.put("isSuccess", "true");
                response.put("message", "Profile completion status updated successfully");
                response.put("data", savedUser);
                return ResponseEntity.ok().body(response);
            } else {

                jobseeker newUser = new jobseeker();

                user alreadyExistingUser = userRepository.findById(userId).get();
                
                newUser.setPersonalDetails(alreadyExistingUser);
                
                newUser.setFullName(fullName);
                if (profilePicture != null)
                    newUser.setProfilePicture(profilePicture);
                newUser.setYearOfExperience(yearOfExperience);
                if (bio != null)
                    newUser.setBio(bio);
                newUser.setPreferredRoles(preferredRoles);


                alreadyExistingUser.setIsProfileCompleted(true);

                jobseeker savedUser = jobseekerRepository.save(newUser);
                userRepository.save(alreadyExistingUser);

                Map<String, Object> response = new HashMap<>();
                response.put("isSuccess", "true");
                response.put("message", "Profile completion status updated successfully");
                response.put("data", savedUser);
                return ResponseEntity.ok().body(response);

            }

        } catch (Exception e) {

            System.out.println("service error");

            Map<String, String> response = new HashMap<>();
            response.put("isSuccess", "false");
            response.put("message", "Error updating profile completion status " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);

        }

    }

}
