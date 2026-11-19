package com.carmatch.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Set;

public record CarSearchRequest(
        @Positive
        BigDecimal maxBudget,
        @PositiveOrZero
        Integer annualMileage,
        Set<TripType> tripTypes,
        FuelType fuelType,
        Boolean homeCharging,
        @Min(1) @Max(9)
        Integer passengers,
        @PositiveOrZero
        Integer minTrunkCapacity,
        BodyStyle bodyStyle
) {
    public CarSearchRequest {
        if (tripTypes != null) {
            tripTypes = Set.copyOf(tripTypes);
        }
    }
}
