package com.example.Carrer_backend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Carrer_backend.Entity.mentor;
import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.mentorRepository;
import com.example.Carrer_backend.Repository.userRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class createMentorProfile {

    private final mentorRepository mentorRepository;
    private final userRepository userRepository;
    private final ObjectMapper objectMapper;

    public createMentorProfile(mentorRepository mentorRepository, userRepository userRepository, ObjectMapper objectMapper) {
        this.mentorRepository = mentorRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<JsonNode> update_mentor_profile(String fullName, String profilePicture, String yearOfMentoring,
            String bio, List<String> expertise, String userId) {

        try {

            if (fullName == null || yearOfMentoring == null || expertise == null) {
                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "Please provide all the required fields");
                return ResponseEntity.badRequest().body(response);
            }

            if (userId == null) {
                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "User ID not found");
                return ResponseEntity.badRequest().body(response);
            }

            if (expertise.isEmpty()) {
                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "Please provide at least one expertise");
                return ResponseEntity.badRequest().body(response);
            }

            Optional<mentor> userDetails = mentorRepository.findById(userId);

            if (userDetails.isPresent()) {
                mentor existingUser = userDetails.get();
                existingUser.setFullName(fullName);
                if (profilePicture != null)
                    existingUser.setProfilePicture(profilePicture);
                existingUser.setYearOfMentoring(yearOfMentoring);
                if (bio != null)
                    existingUser.setBio(bio);
                existingUser.setExpertise(expertise);

                user alreadyExistingUser = userRepository.findById(userId).get();

                alreadyExistingUser.setIsProfileCompleted(true);

                mentor savedUser = mentorRepository.save(existingUser);
                userRepository.save(alreadyExistingUser);

                JsonNode createdUserInfo = objectMapper.createObjectNode()
                        .put("name", savedUser.getFullName())
                        .put("role", alreadyExistingUser.getRole())
                        .put("email", alreadyExistingUser.getEmail())
                        .put("isProfileCompleted", alreadyExistingUser.getIsProfileCompleted())
                        .put("profilePicture", savedUser.getProfilePicture())
                        .put("yearOfMentoring", savedUser.getYearOfMentoring())
                        .put("bio", savedUser.getBio())
                        .set("expertise", objectMapper.valueToTree(savedUser.getExpertise()));

                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "true")
                        .put("message", "Profile completion status updated successfully")
                        .set("data", createdUserInfo);
                return ResponseEntity.ok().body(response);
            } else {
                mentor newUser = new mentor();

                user alreadyExistingUser = userRepository.findById(userId).get();

                newUser.setPersonalDetails(alreadyExistingUser);

                newUser.setFullName(fullName);
                if (profilePicture != null)
                    newUser.setProfilePicture(profilePicture);
                newUser.setYearOfMentoring(yearOfMentoring);
                if (bio != null)
                    newUser.setBio(bio);
                newUser.setExpertise(expertise);

                alreadyExistingUser.setIsProfileCompleted(true);

                mentor savedUser = mentorRepository.save(newUser);
                userRepository.save(alreadyExistingUser);


                JsonNode createdUserInfo = objectMapper.createObjectNode()
                        .put("name", savedUser.getFullName())
                        .put("role", alreadyExistingUser.getRole())
                        .put("email", alreadyExistingUser.getEmail())
                        .put("isProfileCompleted", alreadyExistingUser.getIsProfileCompleted())
                        .put("profilePicture", savedUser.getProfilePicture())
                        .put("yearOfMentoring", savedUser.getYearOfMentoring())
                        .put("bio", savedUser.getBio())
                        .set("expertise", objectMapper.valueToTree(savedUser.getExpertise()));

                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "true")
                        .put("message", "Profile completion status updated successfully")
                        .set("data", createdUserInfo);
                return ResponseEntity.ok().body(response);
            }

        } catch (Exception e) {

            System.out.println("service error");

            JsonNode response = objectMapper.createObjectNode()
                    .put("isSuccess", "false")
                    .put("message", "Error updating profile completion status ")
                    .put("error",e.getMessage());
            return ResponseEntity.internalServerError().body(response);

        }

    }

}
