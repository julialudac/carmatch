package com.carmatch.service;

import com.carmatch.model.Car;
import com.carmatch.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }
    public String getWelcomeMessage() {
        return "Bienvenue sur CarMatch !";
    }

    public Car saveCar(Car car) {
        return this.carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return this.carRepository.findAll();
    }
}
