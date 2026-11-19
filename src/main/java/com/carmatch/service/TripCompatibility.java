package com.carmatch.service;

import com.carmatch.model.enums.BodyStyle;
import com.carmatch.model.enums.TripType;

import java.util.Map;
import java.util.Set;

public class TripCompatibility {
    private static final Map<BodyStyle, Map<TripType, Integer>> scores = Map.ofEntries(
            Map.entry(BodyStyle.HATCHBACK, Map.ofEntries(
                    Map.entry(TripType.CITY, 100),
                    Map.entry(TripType.COUNTRY_ROAD, 70),
                    Map.entry(TripType.HIGHWAY, 60)
            )),
            Map.entry(BodyStyle.SEDAN, Map.ofEntries(
                    Map.entry(TripType.CITY, 75),
                    Map.entry(TripType.COUNTRY_ROAD, 90),
                    Map.entry(TripType.HIGHWAY, 95)
            )),
            Map.entry(BodyStyle.WAGON, Map.ofEntries(
                    Map.entry(TripType.CITY, 75),
                    Map.entry(TripType.COUNTRY_ROAD, 95),
                    Map.entry(TripType.HIGHWAY, 95)
            )),
            Map.entry(BodyStyle.CROSSOVER, Map.ofEntries(
                    Map.entry(TripType.CITY, 85),
                    Map.entry(TripType.COUNTRY_ROAD, 85),
                    Map.entry(TripType.HIGHWAY, 85)
            )),
            Map.entry(BodyStyle.SUV, Map.ofEntries(
                    Map.entry(TripType.CITY, 65),
                    Map.entry(TripType.COUNTRY_ROAD, 85),
                    Map.entry(TripType.HIGHWAY, 90)
            )),
            Map.entry(BodyStyle.MINIVAN, Map.ofEntries(
                    Map.entry(TripType.CITY, 60),
                    Map.entry(TripType.COUNTRY_ROAD, 85),
                    Map.entry(TripType.HIGHWAY, 90)
            ))
    );

    public static int getScore(BodyStyle bodyStyle, TripType tripType) {
        return scores.get(bodyStyle).get(tripType);
    }

    public static int getAverageScore(BodyStyle bodyStyle, Set<TripType> tripTypes) {
        if (tripTypes == null || tripTypes.isEmpty()) {
            return 100;
        }

        return (int) Math.round(tripTypes.stream()
                .mapToInt(tripType -> scores.get(bodyStyle).get(tripType))
                .average()
                .getAsDouble());
    }
}
