package com.example.Carrer_backend.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Carrer_backend.Entity.jobseeker;
import com.example.Carrer_backend.Entity.resume;
import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.jobseekerRepository;
import com.example.Carrer_backend.Repository.mentorRepository;
import com.example.Carrer_backend.Repository.resumeRepository;
import com.example.Carrer_backend.Repository.userRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class dashBoardService {

    private final jobseekerRepository jobseekerRepository;
    private final ObjectMapper objectMapper;
    private final resumeRepository resumeRepository;
    private final userRepository userRepository;
    private final mentorRepository mentorRepository;
    private final apiLogService apiLogService;

    public dashBoardService(jobseekerRepository jobseekerRepository, ObjectMapper objectMapper,
            resumeRepository resumeRepository, userRepository userRepository, mentorRepository mentorRepository, apiLogService apiLogService) {
        this.jobseekerRepository = jobseekerRepository;
        this.objectMapper = objectMapper;
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.mentorRepository = mentorRepository;
        this.apiLogService = apiLogService;
    }

    public ResponseEntity<JsonNode> getDash_board(String userId) {

        try {

            if (userId == null) {

                apiLogService.logApiHit(userId, "api/auth/dashboard", "User not found");
                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "User not found");

                return ResponseEntity.badRequest().body(response);

            }

            Optional<user> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                apiLogService.logApiHit(userId, "api/auth/dashboard", "User not found");
                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "User not found");
                return ResponseEntity.badRequest().body(response);
            }

            String role = user.get().getRole();

            if (role.equals("jobseeker")) {
                Optional<jobseeker> jobseeker = jobseekerRepository.findById(userId);
                List<resume> resumes = resumeRepository.findByUserId(userId);

                if (jobseeker.isEmpty()) {

                    apiLogService.logApiHit(userId, "api/auth/dashboard", "jobseeker profile not created for particular userId");
                    JsonNode response = objectMapper.createObjectNode()
                            .put("isSuccess", "false")
                            .put("message", "jobseeker profile not created for particular userId");

                    return ResponseEntity.badRequest().body(response);

                }

                String fullName = jobseeker.get().getFullName();
                String profilePicture = jobseeker.get().getProfilePicture();
                String yearOfExperience = jobseeker.get().getYearOfExperience();
                String bio = jobseeker.get().getBio();
                List<String> preferredRoles = jobseeker.get().getPreferredRoles();
                JsonNode resumeContent = null;

                if (resumes != null && !resumes.isEmpty()) {
                    resumeContent = resumes.get(resumes.size() - 1).getResumeContent();
                }

                JsonNode skill_field = null;
                if(resumeContent != null){
                    skill_field = resumeContent.get("skills");
                }

                Set<String> skills = new HashSet<>();

                if(skill_field != null){

                    skill_field.forEach(skill -> {
                        skill.forEach(tech ->{
                            skills.add(tech.asText().toLowerCase());
                        });
                    });
                    
                }

                Long mentorMatchCount = mentorRepository.countByExpertise(skills);
                
                

                int completionPercentage = 0;
                boolean fullNamePresent = false;
                boolean profilePicturePresent = false;
                boolean yearOfExperiencePresent = false;
                boolean bioPresent = false;
                boolean preferredRolesPresent = false;
                boolean resumeContentPresent = false;
                boolean skillsPresent = false;

                if (fullName != null) {
                    completionPercentage += 10;
                    fullNamePresent = true;
                }
                if (profilePicture != null) {
                    completionPercentage += 10;
                    profilePicturePresent = true;
                }
                if (yearOfExperience != null) {
                    completionPercentage += 20;
                    yearOfExperiencePresent = true;
                }
                if (bio != null) {
                    completionPercentage += 10;
                    bioPresent = true;
                }
                if (preferredRoles != null) {
                    completionPercentage += 20;
                    preferredRolesPresent = true;
                }
                if (resumeContent != null) {
                    completionPercentage += 20;
                    resumeContentPresent = true;
                }
                if(!skills.isEmpty()){
                    completionPercentage += 10;
                    skillsPresent = true;
                }

                apiLogService.logApiHit(userId, "api/auth/dashboard", "Dashboard fetched successfully");

                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "true")
                        .put("message", "Dashboard fetched successfully")
                        .put("name", fullName)
                        .put("profileCompletionPercentage", completionPercentage)
                        .put("mentorMatch", mentorMatchCount)
                        .put("fullNamePresent", fullNamePresent)
                        .put("profilePicturePresent", profilePicturePresent)
                        .put("skillsPresent", skillsPresent)
                        .put("yearOfExperiencePresent", yearOfExperiencePresent)
                        .put("bioPresent", bioPresent)
                        .put("preferredRolesPresent", preferredRolesPresent)
                        .put("resumeContentPresent", resumeContentPresent);
                        

                return ResponseEntity.ok(response);

            } else {
                apiLogService.logApiHit(userId, "api/auth/dashboard", "dashboard route in progress (only jobseeker can access this route currently)...");

                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "dashboard route in progress (only jobseeker can access this route currently)...");

                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {

            apiLogService.logApiHit(userId, "api/auth/dashboard", "Dashboard not fetched due to " + e.getMessage());
            JsonNode response = objectMapper.createObjectNode()
                    .put("isSuccess", "false")
                    .put("message", "Dashboard not fetched")
                    .put("error", e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }

    }

}
