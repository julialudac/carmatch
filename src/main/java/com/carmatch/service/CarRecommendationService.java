package com.carmatch.service;

import com.carmatch.dto.CarSearchRequest;
import com.carmatch.model.Car;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarRecommendationService {
    private final CarService carService;

    public CarRecommendationService(CarService carService) {
        this.carService = carService;
    }

    public List<Car> findEligibleCars(CarSearchRequest request) {
        var eligibleCars = carService.getAllCars();

        // budget criterion
        if (request.maxBudget() != null) {
            eligibleCars = eligibleCars.stream().filter(car -> request.maxBudget().compareTo(car.getPrice()) >= 0).toList();
        }

        // fuel type criterion
        if (request.fuelType() != null) {
            eligibleCars = eligibleCars.stream().filter(car -> request.fuelType().equals(car.getFuelType())).toList();
        }

        // passenger criterion
        if (request.passengers() != null) {
            eligibleCars = eligibleCars.stream().filter(car -> request.passengers() <= car.getSeats()).toList();
        }

        // trunk capacity criterion
        if (request.minTrunkCapacity() != null) {
            eligibleCars = eligibleCars.stream().filter(car -> request.minTrunkCapacity() <= car.getTrunkCapacity()).toList();
        }

        return eligibleCars;
    }

    int calculateCompatibilityScore(Car car, CarSearchRequest request) {
        int nbCompatibilityCriteria = 0;
        int sumCompatibilityScores = 0;
        if (request.tripTypes() != null && !request.tripTypes().isEmpty()) {
            nbCompatibilityCriteria++;
            sumCompatibilityScores += TripCompatibility.getAverageScore(car.getBodyStyle(), request.tripTypes());
        }
        if (request.bodyStyle() != null) {
            nbCompatibilityCriteria++;
            sumCompatibilityScores += BodyStyleCompatibility.getBodyStyleScore(car.getBodyStyle(), request.bodyStyle());
        }

        return nbCompatibilityCriteria == 0 ? 100 : (int) Math.round((double) sumCompatibilityScores / nbCompatibilityCriteria);
    }
}
