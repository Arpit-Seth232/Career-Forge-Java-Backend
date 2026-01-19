package com.example.Carrer_backend.Service;






import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class signOutUser {

    private final ObjectMapper objectMapper;
    private final apiLogService apiLogService;

    public signOutUser(ObjectMapper objectMapper, apiLogService apiLogService) {
        this.objectMapper = objectMapper;
        this.apiLogService = apiLogService;
    }

    public ResponseEntity<JsonNode> logout(String userId){
        
        try{
        // clear the cookie
        
        

        ResponseCookie delete_cookie = ResponseCookie.from("token", "")
        .path("/")
        .httpOnly(true)
        .sameSite("Lax")
        .maxAge(0)
        .build();

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.SET_COOKIE, delete_cookie.toString());

        JsonNode response = objectMapper.createObjectNode()
                .put("isSuccess", "true")
                .put("message", "User logged out successfully");

        apiLogService.logApiHit(userId, "api/auth/signout", "User logged out successfully");
        
        return ResponseEntity.ok().headers(header).body(response);

        }
        catch(Exception e){
            apiLogService.logApiHit(userId, "api/auth/signout", "Internal Server Error due to " + e.getMessage());

            JsonNode response = objectMapper.createObjectNode()
                    .put("isSuccess", "false")
                    .put("message", "Internal Server Error")
                    .put("error",e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
}
