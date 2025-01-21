package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

import java.sql.Date;
import java.time.LocalDateTime;

public class Ticket {
    private int id;
    private ParkingType parkingType;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private boolean isRecurrent;
    private String licensePlate;
    private String vehicleType;
    private Date inTime;
    private Date outTime;


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

    public Date getInTime() {
        return inTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public boolean isRecurrent() {
        return isRecurrent;
    }

    public void setRecurrent(boolean recurrent) {
        isRecurrent = recurrent;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setInTime(LocalDateTime localDateTime) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setInTime'");
    }

    public void setOutTime(Object outTime2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setOutTime'");
    }

    public void setInTime(java.util.Date inTime2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setInTime'");
    }
}