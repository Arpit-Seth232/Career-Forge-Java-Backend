package com.example.Carrer_backend.Service;

import java.util.*;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Carrer_backend.Entity.resume;
import com.example.Carrer_backend.Entity.resumeEmbeddedId;
import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.resumeRepository;
import com.example.Carrer_backend.Repository.userRepository;
import com.example.Carrer_backend.utility.jsonExtractor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.transaction.Transactional;




@Transactional
@Service
public class uploadResume {

    private final GeminiService geminiService;
    private final resumeRepository resumeRepository;
    private final userRepository userRepository;

    private final ObjectMapper objectMapper;

    public uploadResume(GeminiService geminiService, resumeRepository resumeRepository, userRepository userRepository, ObjectMapper objectMapper) {
        this.geminiService = geminiService;
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<JsonNode> upload_resume(String userId, MultipartFile resume) {

        try {
            if (resume == null) {
                ObjectNode response = objectMapper.createObjectNode();
                response.put("isSuccess", "false");
                response.put("message", "Resume not found");
                return ResponseEntity.badRequest().body(response);
            }

            if (userId == null) {
                ObjectNode response = objectMapper.createObjectNode();
                response.put("isSuccess", "false");
                response.put("message", "User ID not found");
                return ResponseEntity.badRequest().body(response);
            }
            String fileName = resume.getOriginalFilename();

            if (fileName == null || fileName.isEmpty() || fileName.isBlank()) {
                ObjectNode response = objectMapper.createObjectNode();
                response.put("isSuccess", "false");
                response.put("message", "File name not found");
                return ResponseEntity.badRequest().body(response);
            }

            

            byte[] resumeBytes = resume.getBytes();
            String resumeBase64 = Base64.getEncoder().encodeToString(resumeBytes);

            resumeEmbeddedId resumeEmbeddedId = new resumeEmbeddedId();
            resumeEmbeddedId.setUserId(userId);
            resumeEmbeddedId.setFileName(fileName);

            if (resumeRepository.findById(resumeEmbeddedId).isPresent()) {
                resume existingResume = resumeRepository.findById(resumeEmbeddedId).get();

                JsonNode existingResumeData = existingResume.getResumeContent();

                ObjectNode response = objectMapper.createObjectNode();
                response.put("isSuccess", "true");
                response.put("message",
                        "Resume already exists for this user, if you want to update it, please change the file name of the resume then try again");
                response.set("data", existingResumeData);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
            }

            else {
                user existingUser = userRepository.findById(userId).get();

                String responseStr = geminiService.call_gemini(resumeBase64);

                

                String jsonStr = jsonExtractor.extractJson(responseStr);

                if (existingUser == null) {
                    ObjectNode response = objectMapper.createObjectNode();
                    response.put("isSuccess", "false");
                    response.put("message", "User not found");
                    return ResponseEntity.badRequest().body(response);
                }

                

                
                JsonNode jsonNode = objectMapper.readTree(jsonStr);

                

                resume newResume = new resume();
                newResume.setId(resumeEmbeddedId);
                newResume.setResumeContent(jsonNode);
                newResume.setPersonalDetails(existingUser);

                resume savedResume = resumeRepository.save(newResume);

            ObjectNode resultMap = objectMapper.createObjectNode();
            resultMap.put("isSuccess", "true");
            resultMap.put("message", "Resume uploaded successfully");
            resultMap.set("data", savedResume.getResumeContent());

                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(resultMap);
            }

        } catch (Exception e) {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("isSuccess", "false");
            response.put("message", "Error uploading resume " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
