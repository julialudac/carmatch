package com.carmatch.service;

import com.carmatch.model.Car;
import com.carmatch.repository.CarRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @Test
    void shouldReturnWelcomeMessage() {
        String res = carService.getWelcomeMessage();
        assertEquals("Bienvenue sur CarMatch !", res);
    }

    @Test
    void shouldCallRepositoryAndReturnCar() {
        Car car = new Car("Kia", "e-Niro", new BigDecimal(19990));
        when(carRepository.save(car)).thenReturn(car);
        Car carRes = carService.saveCar(car);

        verify(carRepository).save(car);
        assertSame(car, carRes);
    }

    @Test
    void shouldReturnAllCars() {
        Car car1 = new Car("Kia", "e-Niro", new BigDecimal(19990));
        Car car2 = new Car("MG", "MG5", new BigDecimal(16490));
        var cars = List.of(car1, car2);

        when(carRepository.findAll()).thenReturn(cars);
        var res = carService.getAllCars();

        verify(carRepository).findAll();
        assertEquals(cars, res);
    }

    @Test
    void shouldReturnEmptyListWhenNoCarsFound() {
        var emptyCars = List.<Car>of();
        when(carRepository.findAll()).thenReturn(emptyCars);

        var res = carService.getAllCars();

        verify(carRepository).findAll();
        assertTrue(res.isEmpty());
    }

    @Test
    void shouldNotDeleteCarsWhenRetrievingCars() {
        carService.getAllCars();

        verify(carRepository).findAll();
        verify(carRepository, never()).deleteAll();
    }
}
