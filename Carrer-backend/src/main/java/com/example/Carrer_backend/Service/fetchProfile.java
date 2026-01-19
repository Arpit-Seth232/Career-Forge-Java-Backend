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
    private final apiLogService apiLogService; 

    public fetchProfile(userRepository userRepository, jobseekerRepository jobseekerRepository, recruiterRepository recruiterRepository, mentorRepository mentorRepository, ObjectMapper objectMapper, apiLogService apiLogService) {
        this.userRepository = userRepository;
        this.jobseekerRepository = jobseekerRepository;
        this.recruiterRepository = recruiterRepository;
        this.mentorRepository = mentorRepository;
        this.objectMapper = objectMapper;
        this.apiLogService = apiLogService;
    }

    @GetMapping
    public ResponseEntity<JsonNode> getProfile(String userId){

        try{

            if(userId == null){

                apiLogService.logApiHit(userId, "api/auth/profile", "User ID not found");

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

                    JsonNode userInfo = objectMapper.createObjectNode()
                            .put("name", jobseeker.get().getFullName())
                            .put("role", user.getRole())
                            .put("email", user.getEmail())
                            .put("isProfileCompleted", user.getIsProfileCompleted())
                            .put("profilePicture", jobseeker.get().getProfilePicture())
                            .put("yearOfExperience", jobseeker.get().getYearOfExperience())
                            .put("bio", jobseeker.get().getBio())
                            .set("preferredRole", objectMapper.valueToTree(jobseeker.get().getPreferredRoles()));

                    apiLogService.logApiHit(userId, "api/auth/profile", "Profile fetched successfully");

                    JsonNode response = objectMapper.createObjectNode()
                            .put("isSuccess", "true")
                            .put("message", "Profile fetched successfully")
                            .set("data", userInfo);
                    return ResponseEntity.ok().body(response);

                }

                else if(user.getRole().equals("recruiter")){

                    Optional<recruiter> recruiter = recruiterRepository.findById(userId);

                    JsonNode userInfo = objectMapper.createObjectNode()
                            .put("name", recruiter.get().getFullName())
                            .put("role", user.getRole())
                            .put("email", user.getEmail())
                            .put("isProfileCompleted", user.getIsProfileCompleted())
                            .put("profilePicture", recruiter.get().getProfilePicture())
                            .put("jobTitle", recruiter.get().getJobTitle())
                            .put("companySize", recruiter.get().getCompanySize())
                            .put("industry", recruiter.get().getIndustry())
                            .put("companyName", recruiter.get().getCompanyName());

                    apiLogService.logApiHit(userId, "api/auth/profile", "Profile fetched successfully");
                    JsonNode response = objectMapper.createObjectNode()
                            .put("isSuccess", "true")
                            .put("message", "Profile fetched successfully")
                            .set("data", userInfo);
                    return ResponseEntity.ok().body(response);
                    
                }

                else{

                    Optional<mentor> mentor = mentorRepository.findById(userId);

                    JsonNode userInfo = objectMapper.createObjectNode()
                            .put("name", mentor.get().getFullName())
                            .put("role", user.getRole())
                            .put("email", user.getEmail())
                            .put("isProfileCompleted", user.getIsProfileCompleted())
                            .put("profilePicture", mentor.get().getProfilePicture())
                            .put("yearOfMentoring", mentor.get().getYearOfMentoring())
                            .put("bio", mentor.get().getBio())
                            .set("expertise", objectMapper.valueToTree(mentor.get().getExpertise()));

                    apiLogService.logApiHit(userId, "api/auth/profile", "Profile fetched successfully");

                    JsonNode response = objectMapper.createObjectNode()
                            .put("isSuccess", "true")
                            .put("message", "Profile fetched successfully")
                            .set("data", userInfo);
                    return ResponseEntity.ok().body(response);
                    
                }

            }
            else{

                apiLogService.logApiHit(userId, "api/auth/profile", "User mapped to invalid role");

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
            
            apiLogService.logApiHit(userId, "api/auth/profile", "Error fetching profile due to " + e.getMessage());
            
            return ResponseEntity.internalServerError().body(response);
            
        }
        
    }
    
}
