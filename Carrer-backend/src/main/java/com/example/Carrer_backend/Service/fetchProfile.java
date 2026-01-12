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

@Service
public class fetchProfile {

    private final userRepository userRepository;
    private final jobseekerRepository jobseekerRepository;
    private final recruiterRepository recruiterRepository;
    private final mentorRepository mentorRepository;    

    public fetchProfile(userRepository userRepository, jobseekerRepository jobseekerRepository, recruiterRepository recruiterRepository, mentorRepository mentorRepository) {
        this.userRepository = userRepository;
        this.jobseekerRepository = jobseekerRepository;
        this.recruiterRepository = recruiterRepository;
        this.mentorRepository = mentorRepository;
    }

    @GetMapping
    public ResponseEntity<?> getProfile(String userId){

        try{

            if(userId == null){

                Map<String, String> response = new HashMap<>();
                response.put("isSuccess", "false");
                response.put("message", "User ID not found");
                return ResponseEntity.badRequest().body(response);
                
            }

            // System.out.println("user id: " + userId);

            Optional<user> userDetails = userRepository.findById(userId);

            if(userDetails.isPresent()){
                
                user user = userDetails.get();

                if(user.getRole().equals("jobseeker")){
                    

                    Optional<jobseeker> jobseeker = jobseekerRepository.findById(userId);

                    Map<String, Object> response = new HashMap<>();
                    response.put("isSuccess", "true");
                    response.put("message", "Profile fetched successfully");
                    response.put("data", jobseeker.get());
                    return ResponseEntity.ok().body(response);

                }

                else if(user.getRole().equals("recruiter")){

                    Optional<recruiter> recruiter = recruiterRepository.findById(userId);

                    Map<String, Object> response = new HashMap<>();
                    response.put("isSuccess", "true");
                    response.put("message", "Profile fetched successfully");
                    response.put("data", recruiter.get());
                    return ResponseEntity.ok().body(response);
                    
                }

                else{

                    Optional<mentor> mentor = mentorRepository.findById(userId);

                    Map<String, Object> response = new HashMap<>();
                    response.put("isSuccess", "true");
                    response.put("message", "Profile fetched successfully");
                    response.put("data", mentor.get());
                    return ResponseEntity.ok().body(response);
                    
                }

            }
            else{

                Map<String, String> response = new HashMap<>();
                response.put("isSuccess", "false");
                response.put("message", "User ID not found");
                return ResponseEntity.badRequest().body(response);
                
            }
            
        }
        catch(Exception e){

            Map<String, String> response = new HashMap<>();
            response.put("isSuccess", "false");
            response.put("message", "Error fetching profile " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
            
        }
        
    }
    
}
