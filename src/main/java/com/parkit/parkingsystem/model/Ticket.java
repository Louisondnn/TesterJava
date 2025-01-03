package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

import java.time.LocalDateTime;


public class Ticket {
    private int id;
    private ParkingType parkingType;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private LocalDateTime inTime;
    private LocalDateTime outTime;

    public Ticket(String vehicleRegNumber, ParkingSpot parkingSpot, double price) {
        this.vehicleRegNumber = vehicleRegNumber;
        this.parkingSpot = parkingSpot;
        this.parkingType = ParkingType.DEFAULT;
        this.price = price; 

    }
    public ParkingType getParkingType() {
        return parkingType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getInTime() {
        return inTime;
    }
    public void setInTime(LocalDateTime inTime) {
        this.inTime = inTime; // Now accepts LocalDateTime
    }

    public void setOutTime(LocalDateTime outTime) {
        this.outTime = outTime; // This should also accept LocalDateTime
    }

    public LocalDateTime getOutTime() {
        return outTime;
    }

}
