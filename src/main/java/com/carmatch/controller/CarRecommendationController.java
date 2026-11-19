package com.carmatch.controller;

import com.carmatch.dto.CarRecommendation;
import com.carmatch.dto.CarSearchRequest;
import com.carmatch.service.CarRecommendationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CarRecommendationController {
    private final CarRecommendationService carRecommendationService;

    public CarRecommendationController(CarRecommendationService carRecommendationService) {
        this.carRecommendationService = carRecommendationService;
    }

    @PostMapping("/cars/recommendations")
    public ResponseEntity<List<CarRecommendation>> recommendCars(@Valid @RequestBody CarSearchRequest carSearchRequest) {
        return ResponseEntity.ok(this.carRecommendationService.recommendCars(carSearchRequest));
    }
}


