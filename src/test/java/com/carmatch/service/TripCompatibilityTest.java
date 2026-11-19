package com.carmatch.service;

import com.carmatch.model.enums.BodyStyle;
import com.carmatch.model.enums.TripType;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TripCompatibilityTest {
    @Test
    void shouldReturnScore100WithHatchbackAndCity() {
        assertEquals(100, TripCompatibility.getScore(BodyStyle.HATCHBACK, TripType.CITY));
    }

    @Test
    void shouldReturnScore90WithSUVAndHighway() {
        assertEquals(90, TripCompatibility.getScore(BodyStyle.SUV, TripType.HIGHWAY));
    }

    @Test
    void shouldReturnScore95WithWagonAndCountryRoad() {
        assertEquals(95, TripCompatibility.getScore(BodyStyle.WAGON, TripType.COUNTRY_ROAD));
    }

    @Test
    void shouldReturnScore100WithNoTripSelected() {
        assertEquals(100, TripCompatibility.getAverageScore(BodyStyle.HATCHBACK, null));
        assertEquals(100, TripCompatibility.getAverageScore(BodyStyle.HATCHBACK, Set.of()));
    }

    @Test
    void shouldReturn80WithHatchbackAndCityAndHighway() {
        assertEquals(80, TripCompatibility.getAverageScore(BodyStyle.HATCHBACK, Set.of(TripType.CITY, TripType.HIGHWAY)));
    }

    @Test
    void shouldReturn77WithHatchbackAndCityCountryRoadAndHighway() {
        assertEquals(77, TripCompatibility.getAverageScore(BodyStyle.HATCHBACK, Set.of(TripType.CITY, TripType.COUNTRY_ROAD, TripType.HIGHWAY)));
    }

}
