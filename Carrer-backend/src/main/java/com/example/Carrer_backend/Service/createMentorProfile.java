package com.example.Carrer_backend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Carrer_backend.Entity.mentor;
import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.mentorRepository;
import com.example.Carrer_backend.Repository.userRepository;

import java.util.*;

@Service
public class createMentorProfile {

    private final mentorRepository mentorRepository;
    private final userRepository userRepository;

    public createMentorProfile(mentorRepository mentorRepository, userRepository userRepository) {
        this.mentorRepository = mentorRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> update_mentor_profile(String fullName, String profilePicture, String yearOfMentoring,
            String bio, List<String> expertise, String userId) {

                try{

                    if(fullName == null || yearOfMentoring == null || expertise == null){
                        Map<String, String> response = new HashMap<>();
                        response.put("isSuccess", "false");
                        response.put("message", "Please provide all the required fields");
                        return ResponseEntity.badRequest().body(response);
                    }

                    if(userId == null){
                        Map<String, String> response = new HashMap<>();
                        response.put("isSuccess", "false");
                        response.put("message", "User ID not found");
                        return ResponseEntity.badRequest().body(response);
                    }

                    if(expertise.isEmpty()){
                        Map<String, String> response = new HashMap<>();
                        response.put("isSuccess", "false");
                        response.put("message", "Please provide at least one expertise");
                        return ResponseEntity.badRequest().body(response);
                    }

                    Optional<mentor> userDetails = mentorRepository.findById(userId);

                    if(userDetails.isPresent()){
                        mentor existingUser = userDetails.get();
                        existingUser.setFullName(fullName);
                        if(profilePicture != null)
                            existingUser.setProfilePicture(profilePicture);
                        existingUser.setYearOfMentoring(yearOfMentoring);
                        if(bio != null)
                            existingUser.setBio(bio);
                        existingUser.setExpertise(expertise);

                        user alreadyExistingUser = userRepository.findById(userId).get();

                        alreadyExistingUser.setIsProfileCompleted(true);

                        mentor savedUser = mentorRepository.save(existingUser);
                        userRepository.save(alreadyExistingUser);

                        Map<String, Object> response = new HashMap<>();
                        response.put("isSuccess", "true");
                        response.put("message", "Profile completion status updated successfully");
                        response.put("data", savedUser);
                        return ResponseEntity.ok().body(response);
                    }else{
                        mentor newUser = new mentor();

                        user alreadyExistingUser = userRepository.findById(userId).get();
                        
                        newUser.setPersonalDetails(alreadyExistingUser);
                        
                        newUser.setFullName(fullName);
                        if(profilePicture != null)
                            newUser.setProfilePicture(profilePicture);
                        newUser.setYearOfMentoring(yearOfMentoring);
                        if(bio != null)
                            newUser.setBio(bio);
                        newUser.setExpertise(expertise);

                        alreadyExistingUser.setIsProfileCompleted(true);

                        mentor savedUser = mentorRepository.save(newUser);
                        userRepository.save(alreadyExistingUser);

                        Map<String, Object> response = new HashMap<>();
                        response.put("isSuccess", "true");
                        response.put("message", "Profile completion status updated successfully");
                        response.put("data", savedUser);
                        return ResponseEntity.ok().body(response);
                    }

                }catch(Exception e){

                    System.out.println("service error");

                    Map<String, String> response = new HashMap<>();
                    response.put("isSuccess", "false");
                    response.put("message", "Error updating profile completion status " + e.getMessage());
                    return ResponseEntity.internalServerError().body(response);

                }
        
    }
    
}
