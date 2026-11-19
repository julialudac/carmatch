package com.carmatch.dto;

import com.carmatch.model.Car;

public record CarRecommendation(Car car, int compatibilityScore) {
}
