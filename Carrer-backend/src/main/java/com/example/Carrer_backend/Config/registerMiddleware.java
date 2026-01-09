package com.example.Carrer_backend.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.Carrer_backend.Middleware.authMiddleware;

@Configuration
public class registerMiddleware {
    
    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public FilterRegistrationBean<authMiddleware> loggingFilter(){
        FilterRegistrationBean<authMiddleware> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new authMiddleware(secret));
        registrationBean.addUrlPatterns("/api/auth/*");

        registrationBean.setOrder(1);
        return registrationBean;
    }
    
}
