package com.example.Carrer_backend.Service;

import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.example.Carrer_backend.Entity.mentor;
import com.example.Carrer_backend.Entity.resume;
import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.mentorRepository;
import com.example.Carrer_backend.Repository.resumeRepository;
import com.example.Carrer_backend.Repository.userRepository;

@Service
public class mentorMatchService {

    private final ObjectMapper objectMapper;
    private final userRepository userRepository;
    private final resumeRepository resumeRepository;
    private final mentorRepository mentorRepository;    

    public mentorMatchService(ObjectMapper objectMapper, userRepository userRepository, resumeRepository resumeRepository, mentorRepository mentorRepository) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.resumeRepository = resumeRepository;
        this.mentorRepository = mentorRepository;
    }

    public ResponseEntity<JsonNode> fetchMentors(String userId){

        try{
        if(userId == null){

            ObjectNode response = objectMapper.createObjectNode();
            response.put("isSuccess", false);
            response.put("message", "User not found");

            return ResponseEntity.badRequest().body(response);

        }

        Optional<user> user = userRepository.findById(userId);

        if(user.isEmpty()){

            ObjectNode response = objectMapper.createObjectNode();
            response.put("isSuccess", false);
            response.put("message", "invalid user id, no user found");

            return ResponseEntity.badRequest().body(response);

        }

        String role = user.get().getRole();

        if(role.equals("jobseeker")){

            List<resume> resumes = resumeRepository.findByUserId(userId);

            if(resumes == null || resumes.isEmpty()){

                ObjectNode response = objectMapper.createObjectNode();
                response.put("isSuccess", false);
                response.put("message", "resume not found, please add a resume, no mentor matches found");

                return ResponseEntity.badRequest().body(response);

            }

            JsonNode resumeContent = null;

            
            resumeContent = resumes.get(resumes.size() - 1).getResumeContent();
            


            JsonNode skills = resumeContent.get("skills");

            Set<String> skillSet = new HashSet<>();

            skills.forEach(field -> field.forEach(skill -> skillSet.add(skill.asText().toLowerCase())));

            List<mentor> mentors = mentorRepository.findByExpertise(skillSet);

            if(mentors == null || mentors.isEmpty()){

                ObjectNode response = objectMapper.createObjectNode();
                response.put("isSuccess", false);
                response.put("message", "no mentor matches found");

                return ResponseEntity.badRequest().body(response);

            }

            List<ObjectNode> mentorsMatchNode = new ArrayList<>();
            

            mentors.forEach(mentor -> {

                ObjectNode mentorNode = objectMapper.createObjectNode();
                mentorNode.put("name", mentor.getFullName());
                mentorNode.put("profilePicture", mentor.getProfilePicture());
                mentorNode.put("yearOfMentoring", mentor.getYearOfMentoring());
                mentorNode.put("bio", mentor.getBio());
                mentorNode.set("expertise", objectMapper.valueToTree(mentor.getExpertise())); 

                mentorsMatchNode.add(mentorNode);

                
            });


            ObjectNode response = objectMapper.createObjectNode();
            response.put("isSuccess", true);
            response.put("message", "mentor matches found");
            response.set("mentors", objectMapper.valueToTree(mentorsMatchNode));
            return ResponseEntity.ok().body(response);

        }
        else{

            ObjectNode response = objectMapper.createObjectNode();
            response.put("isSuccess", false);
            response.put("message", "user is not a jobseeker");

            return ResponseEntity.badRequest().body(response);

        }
    }
    catch(Exception e){

        ObjectNode response = objectMapper.createObjectNode();
        response.put("isSuccess", false);
        response.put("message", "internal server error");
        response.put("error", e.getMessage());

        return ResponseEntity.badRequest().body(response);

    }

        
    }
    
}
