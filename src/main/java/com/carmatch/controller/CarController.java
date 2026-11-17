package com.carmatch.controller;

import com.carmatch.model.Car;
import com.carmatch.service.CarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    public List<Car> cars() {
        return this.carService.getAllCars();
    }

    @PostMapping("/cars")
    public Car insertCar(@RequestBody Car car) {
        return this.carService.saveCar(car);
    }

}
