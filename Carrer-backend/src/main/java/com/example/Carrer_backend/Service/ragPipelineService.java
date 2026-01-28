package com.example.Carrer_backend.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class ragPipelineService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ragPipelineService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public JsonNode hitPipeline(String userId, String jd, JsonNode resumeContent, String fileName) {

        try {
            ObjectNode requestBody = objectMapper.createObjectNode()
                    .put("userId", userId)
                    .put("jd", jd)
                    .put("fileName", fileName)
                    .set("resumeContent", resumeContent);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<JsonNode> response = restTemplate.postForEntity("http://localhost:9090/rag/analyze",
                    requestEntity, JsonNode.class);

            return response.getBody();

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public JsonNode getSimilarJds(String userId, double[] resumeEmbedding) {
        try {
            ObjectNode requestBody = objectMapper.createObjectNode()
                    .put("userId", userId)
                    .set("resumeEmbedding", objectMapper.valueToTree(resumeEmbedding));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<JsonNode> response = restTemplate
                    .postForEntity("http://localhost:9090/search/similarity-search", requestEntity, JsonNode.class);

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
