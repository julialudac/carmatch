package com.carmatch.service;

import com.carmatch.dto.CarSearchRequest;
import com.carmatch.model.Car;
import com.carmatch.model.enums.BodyStyle;
import com.carmatch.model.enums.FuelType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarRecommendationServiceTest {
    @Mock
    private CarService carService;

    @InjectMocks
    private CarRecommendationService carRecommendationService;

    @Nested
    class EligibleCarsTests {
        @Test
        void shouldKeepCarThatMatchesAllCriteria() {
            Car car = new Car("Kia", "e-Niro", new BigDecimal("19990"), FuelType.ELECTRIC, BodyStyle.CROSSOVER, 5, 451);
            when(carService.getAllCars()).thenReturn(List.of(car));

            CarSearchRequest request = new CarSearchRequest(new BigDecimal("20000"), null, FuelType.ELECTRIC, 5, 300, null);
            var res = carRecommendationService.findEligibleCars(request);

            assertEquals(List.of(car), res);
        }
    }

    @Nested
    class IneligibleCarsTests {
        @Test
        void shouldEliminateCarThatFailsBudgetCriterion() {
            Car car = new Car("Kia", "e-Niro", new BigDecimal("19990"), FuelType.ELECTRIC, BodyStyle.CROSSOVER, 5, 451);
            when(carService.getAllCars()).thenReturn(List.of(car));

            CarSearchRequest request = new CarSearchRequest(new BigDecimal("15000"), null, FuelType.ELECTRIC, 5, 300, null);
            var res = carRecommendationService.findEligibleCars(request);

            assertTrue(res.isEmpty());
        }

        @Test
        void shouldEliminateCarThatFailsTrunkCapacityCriterion() {
            Car car = new Car("Kia", "e-Niro", new BigDecimal("19990"), FuelType.ELECTRIC, BodyStyle.CROSSOVER, 5, 451);
            when(carService.getAllCars()).thenReturn(List.of(car));

            CarSearchRequest request = new CarSearchRequest(new BigDecimal("20000"), null, FuelType.ELECTRIC, 5, 500, null);
            var res = carRecommendationService.findEligibleCars(request);

            assertTrue(res.isEmpty());
        }

        @Test
        void shouldEliminateCarThatFailsPassengersCriterion() {
            Car car = new Car("Kia", "e-Niro", new BigDecimal("19990"), FuelType.ELECTRIC, BodyStyle.CROSSOVER, 5, 451);
            when(carService.getAllCars()).thenReturn(List.of(car));

            CarSearchRequest request = new CarSearchRequest(new BigDecimal("20000"), null, FuelType.ELECTRIC, 6, 300, null);
            var res = carRecommendationService.findEligibleCars(request);

            assertTrue(res.isEmpty());
        }

        @Test
        void shouldEliminateCarThatFailsFuelTypeCriterion() {
            Car car = new Car("Kia", "e-Niro", new BigDecimal("19990"), FuelType.ELECTRIC, BodyStyle.CROSSOVER, 5, 451);
            when(carService.getAllCars()).thenReturn(List.of(car));

            CarSearchRequest request = new CarSearchRequest(new BigDecimal("20000"), null, FuelType.DIESEL, 5, 300, null);
            var res = carRecommendationService.findEligibleCars(request);

            assertTrue(res.isEmpty());
        }
    }
}
