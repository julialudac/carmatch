package com.carmatch.service;

import org.springframework.stereotype.Service;

@Service
public class CarService {
    public String getWelcomeMessage() {
        return "Bienvenue sur CarMatch !";
    }
}
