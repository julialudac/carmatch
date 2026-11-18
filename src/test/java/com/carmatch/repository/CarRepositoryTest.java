package com.carmatch.repository;

import com.carmatch.model.Car;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository;

    @Test
    void shouldInjectRepository() {
        assertNotNull(carRepository);
    }

    @Test
    void shouldSaveCar() {
        Car car = new Car("Kia", "e-Niro", new BigDecimal("19990"));

        Car savedCar = carRepository.save(car);

        assertNotNull(savedCar.getId());
    }

    @Test
    void shouldFindAllCars() {
        Car car1 = new Car("Kia", "e-Niro", new BigDecimal("19990"));
        Car car2 = new Car("MG", "MG5", new BigDecimal("16490"));

        carRepository.save(car1);
        carRepository.save(car2);

        var cars = carRepository.findAll();

        assertEquals(2, cars.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoCarsExist() {
        var cars = carRepository.findAll();

        assertTrue(cars.isEmpty());
    }

    @Test
    void shouldRetrieveAddedCarById() {
        Car car = new Car("Kia", "e-Niro", new BigDecimal(19990));

        var savedCar = carRepository.save(car);
        Optional<Car> retrievedCar = carRepository.findById(savedCar.getId());

        assertTrue(retrievedCar.isPresent());
        assertEquals("e-Niro", retrievedCar.get().getModel());
    }
}
