package com.example.Carrer_backend.Service;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Carrer_backend.Entity.jobseeker;
import com.example.Carrer_backend.Entity.mentor;
import com.example.Carrer_backend.Entity.recruiter;
import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.jobseekerRepository;
import com.example.Carrer_backend.Repository.mentorRepository;
import com.example.Carrer_backend.Repository.recruiterRepository;
import com.example.Carrer_backend.Repository.userRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class fetchProfile {

    private final userRepository userRepository;
    private final jobseekerRepository jobseekerRepository;
    private final recruiterRepository recruiterRepository;
    private final mentorRepository mentorRepository;
    private final ObjectMapper objectMapper;    

    public fetchProfile(userRepository userRepository, jobseekerRepository jobseekerRepository, recruiterRepository recruiterRepository, mentorRepository mentorRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.jobseekerRepository = jobseekerRepository;
        this.recruiterRepository = recruiterRepository;
        this.mentorRepository = mentorRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<JsonNode> getProfile(String userId){

        try{

            if(userId == null){

                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "User ID not found");
                return ResponseEntity.badRequest().body(response);
                
            }

            // System.out.println("user id: " + userId);

            Optional<user> userDetails = userRepository.findById(userId);

            if(userDetails.isPresent()){
                
                user user = userDetails.get();

                if(user.getRole().equals("jobseeker")){
                    

                    Optional<jobseeker> jobseeker = jobseekerRepository.findById(userId);

                    JsonNode response = objectMapper.createObjectNode()
                            .put("isSuccess", "true")
                            .put("message", "Profile fetched successfully")
                            .set("data", objectMapper.valueToTree(jobseeker.get()));
                    return ResponseEntity.ok().body(response);

                }

                else if(user.getRole().equals("recruiter")){

                    Optional<recruiter> recruiter = recruiterRepository.findById(userId);

                    JsonNode response = objectMapper.createObjectNode()
                            .put("isSuccess", "true")
                            .put("message", "Profile fetched successfully")
                            .set("data", objectMapper.valueToTree(recruiter.get()));
                    return ResponseEntity.ok().body(response);
                    
                }

                else{

                    Optional<mentor> mentor = mentorRepository.findById(userId);

                    JsonNode response = objectMapper.createObjectNode()
                            .put("isSuccess", "true")
                            .put("message", "Profile fetched successfully")
                            .set("data", objectMapper.valueToTree(mentor.get()));
                    return ResponseEntity.ok().body(response);
                    
                }

            }
            else{

                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "user mapped to invalid role");
                return ResponseEntity.badRequest().body(response);
                
            }
            
        }
        catch(Exception e){

            JsonNode response = objectMapper.createObjectNode()
                    .put("isSuccess", "false")
                    .put("message", "Error fetching profile ")
                    .put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
            
        }
        
    }
    
}
