package com.carmatch.controller;

import com.carmatch.model.Car;
import com.carmatch.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Car>> cars() {
        return ResponseEntity.ok(this.carService.getAllCars());
    }

    @PostMapping("/cars")
    public ResponseEntity<Car> insertCar(@Valid @RequestBody Car car) {
        return new ResponseEntity<>(this.carService.saveCar(car), HttpStatus.CREATED);
    }

}
