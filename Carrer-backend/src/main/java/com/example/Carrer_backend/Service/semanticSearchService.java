package com.example.Carrer_backend.Service;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Carrer_backend.Entity.resume;
import com.example.Carrer_backend.Entity.user;
import com.example.Carrer_backend.Repository.resumeRepository;
import com.example.Carrer_backend.Repository.userRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class semanticSearchService {

    private final apiLogService apiLogService;
    private final ObjectMapper objectMapper;
    private final userRepository userRepository;
    private final resumeRepository resumeRepository;
    private final ragPipelineService ragPipelineService;

    public semanticSearchService(apiLogService apiLogService, ObjectMapper objectMapper, userRepository userRepository, resumeRepository resumeRepository, ragPipelineService ragPipelineService) {
        this.apiLogService = apiLogService;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.resumeRepository = resumeRepository;
        this.ragPipelineService = ragPipelineService;
    }

    public ResponseEntity<JsonNode> analyze(String userId, MultipartFile jd){

        try{
            
            if(userId == null){
                
                ObjectNode response = objectMapper.createObjectNode()
                .put("isSuccess", "false")
                .put("message", "User not found");

                apiLogService.logApiHit("unknown user", "api/auth/similaritySearch", "User not found");

                return ResponseEntity.badRequest().body(response);
            }

            Optional<user> user = userRepository.findById(userId);

            if(user.isEmpty()){
                
                ObjectNode response = objectMapper.createObjectNode()
                .put("isSuccess", "false")
                .put("message", "Invalid user id");

                apiLogService.logApiHit(userId, "api/auth/similaritySearch", "Invalid user id");

                return ResponseEntity.badRequest().body(response);
            }

            List<resume> resumes = resumeRepository.findByUserId(userId);

            if(resumes.isEmpty()){
                
                ObjectNode response = objectMapper.createObjectNode()
                .put("isSuccess", "false")
                .put("message", "No resume found, firstly go to upload resume section and upload your resume");

                apiLogService.logApiHit(userId, "api/auth/similaritySearch", "No resume found");

                return ResponseEntity.badRequest().body(response);
            }

            JsonNode resumeContent = resumes.get(resumes.size()-1).getResumeContent();

            String fileName = resumes.get(resumes.size()-1).getId().getFileName();

            byte[] jdBytes = jd.getBytes();

            String encodedJd = Base64.getEncoder().encodeToString(jdBytes);

            JsonNode response = ragPipelineService.hitPipeline(userId,encodedJd, resumeContent,fileName);

            ObjectNode objectNode = objectMapper.createObjectNode()
            .put("isSuccess", "true")
            .put("message", "Success")
            .set("data", response);

            apiLogService.logApiHit(userId, "api/auth/similaritySearch", "Success");

            return ResponseEntity.ok(objectNode);



        }
        catch(Exception e){

            ObjectNode response = objectMapper.createObjectNode()
            .put("isSuccess", "false")
            .put("message", "Internal Server Error")
            .put("error",e.getMessage());

            apiLogService.logApiHit(userId, "api/auth/similaritySearch", "Internal Server Error due to " + e.getMessage());

            return ResponseEntity.internalServerError().body(response);
            
        }
        
    }

    
    

}
