package com.carmatch.controller;

import com.carmatch.service.CarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final CarService carService;

    public HelloController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/hello")
    public String hello() { return carService.getWelcomeMessage(); }
}
