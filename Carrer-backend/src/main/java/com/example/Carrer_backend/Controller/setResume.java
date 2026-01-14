package com.example.Carrer_backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Carrer_backend.Service.uploadResume;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth/resume")
public class setResume {

    private final uploadResume uploadResume;

    public setResume(uploadResume uploadResume) {
        this.uploadResume = uploadResume;
    }

    @PostMapping
    public ResponseEntity<JsonNode> takeResume(@RequestParam("resume") MultipartFile resume, HttpServletRequest request){

        String userId = (String) request.getAttribute("userId");

        return uploadResume.upload_resume(userId, resume);
        
    }
    
}
