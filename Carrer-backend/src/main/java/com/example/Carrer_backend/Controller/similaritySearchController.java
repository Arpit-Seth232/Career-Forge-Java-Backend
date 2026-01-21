package com.example.Carrer_backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Carrer_backend.Service.semanticSearchService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth/similaritySearch")
public class similaritySearchController {

    private final semanticSearchService semanticSearchService;

    public similaritySearchController(semanticSearchService semanticSearchService) {
        this.semanticSearchService = semanticSearchService;
    }

    @PostMapping
    public ResponseEntity<JsonNode> similaritySearch(HttpServletRequest request,@RequestParam("jd") MultipartFile jd){
        String userId = (String) request.getAttribute("userId");

        return semanticSearchService.analyze(userId,jd);
    }
    
}
