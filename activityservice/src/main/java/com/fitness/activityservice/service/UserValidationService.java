package com.fitness.activityservice.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
public class UserValidationService {
    private final WebClient userServiceWebClient;

    public UserValidationService(@Qualifier("userServiceWebClient") WebClient userServiceWebClient) {
        this.userServiceWebClient = userServiceWebClient;
    }

    public boolean validateUser(String userId){
        log.info("Calling user validation API for userId: {}", userId);
        try {
            return Boolean.TRUE.equals(userServiceWebClient.get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve().bodyToMono(Boolean.class)
                    .block());

        }catch (WebClientResponseException e){

             if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                 throw new RuntimeException("User not found: " + userId);
             }else if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                 throw new RuntimeException("Invalid Request: " + userId);
             }
        }
       return  false;
    }
}
