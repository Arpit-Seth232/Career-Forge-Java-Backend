package com.example.Carrer_backend.Service;

import org.springframework.stereotype.Service;

import com.example.Carrer_backend.Entity.apiLogs;
import com.example.Carrer_backend.Repository.apiLogRepository;

@Service
public class apiLogService {
    
    private final apiLogRepository apiLogRepository;
    
    public apiLogService(apiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }
    

    public void logApiHit(String userId, String apiHitName, String apiHitResponse) {

        try{
        apiLogs log = new apiLogs();
        log.setUserId(userId);
        log.setApiHitName(apiHitName);
        log.setApiHitResponse(apiHitResponse);
        
        apiLogRepository.save(log);
        
        }catch(Exception e){
            
            System.out.println("Failed to log API hit " + apiHitName + " for user " + userId + " : " + e.getMessage());

        }
    }

}
