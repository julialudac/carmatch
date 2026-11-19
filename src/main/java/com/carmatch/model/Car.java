package com.carmatch.model;

import com.carmatch.model.enums.BodyStyle;
import com.carmatch.model.enums.FuelType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La marque est obligatoire")
    private String brand;

    @NotBlank
    private String model;

    @NotNull
    @Positive
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @NotNull
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BodyStyle bodyStyle;

    @Min(1) @Max(9)
    @NotNull
    private Integer seats;

    @PositiveOrZero
    @NotNull
    private Integer trunkCapacity;

    protected Car() {
    }

    public Car(String brand, String model, BigDecimal price, FuelType fuelType, BodyStyle bodyStyle, Integer seats, Integer trunkCapacity) {
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.fuelType = fuelType;
        this.bodyStyle = bodyStyle;
        this.seats = seats;
        this.trunkCapacity = trunkCapacity;
    }

    public Long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public BodyStyle getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(BodyStyle bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Integer getTrunkCapacity() {
        return trunkCapacity;
    }

    public void setTrunkCapacity(Integer trunkCapacity) {
        this.trunkCapacity = trunkCapacity;
    }
}
