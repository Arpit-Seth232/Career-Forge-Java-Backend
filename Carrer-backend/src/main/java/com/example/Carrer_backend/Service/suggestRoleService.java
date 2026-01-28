package com.example.Carrer_backend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Carrer_backend.Entity.resume;
import com.example.Carrer_backend.Entity.suggestRole;
import com.example.Carrer_backend.Repository.resumeEmbedRepository;
import com.example.Carrer_backend.Repository.resumeRepository;
import com.example.Carrer_backend.Repository.suggestRolesRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;

@Service
public class suggestRoleService {

    private final ObjectMapper objectMapper;
    private final resumeRepository resumeRepository;
    private final resumeEmbedRepository resumeEmbedRepository;
    private final ragPipelineService ragPipelineService;
    private final suggestRolesRepository suggestRolesRepository;
    private final apiLogService apiLogService;

    public suggestRoleService(ObjectMapper objectMapper, resumeRepository resumeRepository,
            resumeEmbedRepository resumeEmbedRepository, ragPipelineService ragPipelineService,
            suggestRolesRepository suggestRolesRepository, apiLogService apiLogService) {
        this.objectMapper = objectMapper;
        this.resumeRepository = resumeRepository;
        this.resumeEmbedRepository = resumeEmbedRepository;
        this.ragPipelineService = ragPipelineService;
        this.suggestRolesRepository = suggestRolesRepository;
        this.apiLogService = apiLogService;
    }

    public ResponseEntity<JsonNode> getRoles(String userId) {

        try {

            if (userId == null) {
                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "User ID not found");

                apiLogService.logApiHit("/api/auth/suggestRoles", "unknown", "User ID not found");
                return ResponseEntity.badRequest().body(response);
            }

            // System.out.println(userId);

            List<resume> resumes = resumeRepository.findByUserId(userId);

            if (resumes.isEmpty()) {
                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "No resumes found, try after uploading resume");

                apiLogService.logApiHit("/api/auth/suggestRoles", userId,
                        "No resumes found, try after uploading resume");
                return ResponseEntity.badRequest().body(response);
            }

            Optional<suggestRole> existingResponse = suggestRolesRepository.findByUserIdAndFileName(userId,
                    resumes.get(resumes.size() - 1).getId().getFileName());
            if (existingResponse.isPresent()) {

                apiLogService.logApiHit("/api/auth/suggestRoles", userId, "Recommendations fetched successfully");

                return ResponseEntity.ok(existingResponse.get().getSuggestedRolesResponse());

            }

            JsonNode suggestedRoles = resumes.get(resumes.size() - 1).getResumeContent().get("recommendations")
                    .get("suggested_roles");

            ArrayList<?> embeddings = resumeEmbedRepository.findByUserId(userId);

            if (embeddings.isEmpty()) {
                JsonNode response = objectMapper.createObjectNode()
                        .put("isSuccess", "false")
                        .put("message", "No embeddings found for this user");

                apiLogService.logApiHit("/api/auth/suggestRoles", userId, "No resume embeddings found for this user");
                return ResponseEntity.badRequest().body(response);
            }

            JsonNode resume_embedding = objectMapper.valueToTree(embeddings.get(embeddings.size() - 1));

            String embedding = resume_embedding.get("value").asText();

            String substr_embed = embedding.substring(1, embedding.length() - 1);

            double[] embeddingArray = Arrays.stream(substr_embed.split(",")).mapToDouble(Double::parseDouble).toArray();

            JsonNode similarJds = ragPipelineService.getSimilarJds(userId, embeddingArray);

            ObjectNode response = objectMapper.createObjectNode();
            response.put("isSuccess", "true");
            response.put("message", "Recommendations fetched successfully");

            ObjectNode data = objectMapper.createObjectNode();
            data.set("suggestedRoles", suggestedRoles);
            data.set("similarJds", similarJds);

            response.set("suggestedJobs", data);

            suggestRole suggestRole = new suggestRole();
            suggestRole.setUserId(userId);
            suggestRole.setFileName(resumes.get(resumes.size() - 1).getId().getFileName());
            suggestRole.setResume(resumes.get(resumes.size() - 1));
            suggestRole.setSuggestedRolesResponse(response);

            suggestRolesRepository.save(suggestRole);

            apiLogService.logApiHit("/api/auth/suggestRoles", userId, "Recommendations fetched successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            JsonNode response = objectMapper.createObjectNode()
                    .put("isSuccess", "false")
                    .put("message", "Something went wrong")
                    .put("error", e.getMessage());

            apiLogService.logApiHit("/api/auth/suggestRoles", userId, "Something went wrong " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

    }
}
