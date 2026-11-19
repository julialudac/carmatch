package com.carmatch.service;

import com.carmatch.dto.CarSearchRequest;
import com.carmatch.model.Car;
import com.carmatch.model.enums.BodyStyle;
import com.carmatch.model.enums.FuelType;
import com.carmatch.model.enums.TripType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Nested
    class CompatibilityScoreTests {
        @Test
        void shouldReturn100WhenNoPreferenceSpecified() {
            Car car = new Car("Kia", "e-Niro", new BigDecimal("19990"), FuelType.ELECTRIC, BodyStyle.CROSSOVER, 5, 451);
            CarSearchRequest request = new CarSearchRequest(null, null, null, null, null, null);

            int score = carRecommendationService.calculateCompatibilityScore(car, request);

            assertEquals(100, score);
        }

        @Test
        void shouldReturn80WhenOnlyTripTypesSpecified() {
            Car car = new Car("Honda", "Honda Jazz", new BigDecimal("9990"), FuelType.HYBRID, BodyStyle.HATCHBACK, 5, 300);
            CarSearchRequest request = new CarSearchRequest(null, Set.of(TripType.CITY, TripType.HIGHWAY), null, null, null, null);

            int score = carRecommendationService.calculateCompatibilityScore(car, request);

            assertEquals(80, score);
        }

        @Test
        void shouldReturn70WhenTargetBodyStyleDoesNotMatch() {
            Car car = new Car("Honda", "Honda Jazz", new BigDecimal("9990"), FuelType.HYBRID, BodyStyle.HATCHBACK, 5, 300);
            CarSearchRequest request = new CarSearchRequest(null, null, null, null, null, BodyStyle.SUV);

            int score = carRecommendationService.calculateCompatibilityScore(car, request);

            assertEquals(70, score);
        }

        @Test
        void shouldReturn100WhenTargetBodyStyleMatches() {
            Car car = new Car("Honda", "Honda Jazz", new BigDecimal("9990"), FuelType.HYBRID, BodyStyle.HATCHBACK, 5, 300);
            CarSearchRequest request = new CarSearchRequest(null, null, null, null, null, BodyStyle.HATCHBACK);

            int score = carRecommendationService.calculateCompatibilityScore(car, request);

            assertEquals(100, score);
        }

        @Test
        void shouldReturn75WhenTargetBodyStyleDoesNotMatchAndTripTypesSpecified() {
            Car car = new Car("Honda", "Honda Jazz", new BigDecimal("9990"), FuelType.HYBRID, BodyStyle.HATCHBACK, 5, 300);
            CarSearchRequest request = new CarSearchRequest(null, Set.of(TripType.CITY, TripType.HIGHWAY), null, null, null, BodyStyle.SUV);

            int score = carRecommendationService.calculateCompatibilityScore(car, request);

            assertEquals(75, score);
        }
    }

    @Nested
    class RankCarsTests {

        @Test
        void shouldReturnCarsInDescendingOrderOfCompatibilityScore() {
            Car car1 = new Car("Honda", "Honda Jazz", new BigDecimal("9990"), FuelType.HYBRID, BodyStyle.HATCHBACK, 5, 300);
            Car car2 = new Car("Kia", "e-Niro", new BigDecimal("19990"), FuelType.ELECTRIC, BodyStyle.CROSSOVER, 5, 451);
            CarSearchRequest request = new CarSearchRequest(null, Set.of(TripType.CITY), null, null, null, BodyStyle.HATCHBACK);

            var res = carRecommendationService.rankCars(List.of(car2, car1), request);

            assertEquals("Honda Jazz", res.get(0).car().getModel());
            assertEquals(100, res.get(0).compatibilityScore());
            assertEquals("e-Niro", res.get(1).car().getModel());
            assertEquals(78, res.get(1).compatibilityScore());
        }

        @Test
        void shouldReturnEmptyListWhenNoEligibleCars() {
            CarSearchRequest request = new CarSearchRequest(null, null, null, null, null, null);

            var res = carRecommendationService.rankCars(List.of(), request);

            assertTrue(res.isEmpty());
        }
    }

    @Nested
    class RecommendCarsTests {
        @Test
        void shouldFilterOutCarsThatDoNotMeetTheCriteria() {
            Car car1 = new Car("Honda", "Honda Jazz", new BigDecimal("9990"), FuelType.HYBRID, BodyStyle.HATCHBACK, 5, 300);
            Car car2 = new Car("Kia", "e-Niro", new BigDecimal("19990"), FuelType.ELECTRIC, BodyStyle.CROSSOVER, 5, 451);
            CarSearchRequest request = new CarSearchRequest(new BigDecimal("15000"), null, null, null, null, null);

            when(carService.getAllCars()).thenReturn(List.of(car1, car2));
            var res = carRecommendationService.recommendCars(request);

            assertEquals(1, res.size());
            assertEquals("Honda Jazz", res.getFirst().car().getModel());
            assertEquals(100, res.getFirst().compatibilityScore());
        }
    }
}
