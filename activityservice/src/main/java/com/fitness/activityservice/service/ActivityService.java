package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    public ActivityResponse trackActivity(ActivityRequest request) {

        boolean inValidUser = userValidationService.validateUser(request.getUserId());
        if(!inValidUser){
            throw new RuntimeException("Invalid user with Id " + request.getUserId());
        }
         Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .durration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);

        return mapToResponse(savedActivity);

    }
    private ActivityResponse mapToResponse(Activity activity){
           ActivityResponse response = new ActivityResponse();
           response.setId(activity.getId());
           response.setType(activity.getType());
           response.setDurration(activity.getDurration());
           response.setCreatedAt(activity.getCreatedAt());
           response.setCaloriesBurned(activity.getCaloriesBurned());
           response.setAdditionalMetrics(activity.getAdditionalMetrics());
           response.setUpdatedAt(activity.getUpdatedAt());
           response.setStartTime(activity.getStartTime());
           response.setUserId(activity.getUserId());

           return response;

    }

    public List<ActivityResponse> getUserActivites(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);

        return  activities.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String activityId) {
        return activityRepository.findById(activityId).map(this::mapToResponse).orElseThrow(()-> new RuntimeException("Activity not found"));
    }
}
