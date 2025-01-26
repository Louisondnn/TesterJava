package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Date;
import java.time.Duration;
import static com.parkit.parkingsystem.constants.Fare.*;

public class FareCalculatorService {

    // private static final int MILLISECONDS_TO_HOUR = 1000 * 60 * 60;

    public static LocalDateTime convertToLocalDateTime(LocalDateTime date) {
        return date.toInstant(null).atZone(ZoneId.systemDefault()).toLocalDateTime();
}
    public static void calculateFare(Ticket ticket, boolean discount) {
        if (ticket.getOutTime() == null || ticket.getInTime() == null || ticket.getOutTime().isBefore(ticket.getInTime())) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime());
        }

        LocalDateTime inTime = convertToLocalDateTime(ticket.getInTime());
        LocalDateTime outTime = convertToLocalDateTime(ticket.getOutTime());
    
        Duration duration = Duration.between(inTime, outTime);
        double hours = duration.toMinutes() / 60.0; // Convertir la durée en heures
    
        double discountCar = 0.0;
        double discountBike = 0.0;

     if (discount) {
            discountCar = Fare.DISCOUNT_RATE * hours * Fare.CAR_RATE_PER_HOUR;
            discountBike = Fare.DISCOUNT_RATE * hours * Fare.BIKE_RATE_PER_HOUR;
        }

        if (hours < 0.5) {
            ticket.setPrice(0.0);
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR:
                    ticket.setPrice(hours * Fare.CAR_RATE_PER_HOUR - discountCar);
                    break;
                case BIKE:
                    ticket.setPrice(hours * Fare.BIKE_RATE_PER_HOUR - discountBike);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
    }
}