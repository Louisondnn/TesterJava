package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Date;
import java.time.Duration;
import static com.parkit.parkingsystem.constants.Fare.*;
import java.time.temporal.ChronoUnit;

public class FareCalculatorService {

    private static final double MILLISECONDS_TO_HOUR = 1000 * 60 * 60;
        public static LocalDateTime convertToLocalDateTime(LocalDateTime date) {
            return date.toInstant(null).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
        public static void calculateFare(Ticket ticket, boolean discount) {
            if (ticket.getOutTime() == null || ticket.getInTime() == null || ticket.getOutTime().before(ticket.getInTime())) {
                throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime());
            }
    
            double inHour = ticket.getInTime().getTime();
            double outHour = ticket.getOutTime().getTime();
    
            double discountCar = 0.0;
            double discountBike = 0.0;
    
            double duration = (outHour - inHour) / MILLISECONDS_TO_HOUR;

        if (discount) {
            discountCar = DISCOUNT_RATE * duration * CAR_RATE_PER_HOUR;
            discountBike = DISCOUNT_RATE * duration * BIKE_RATE_PER_HOUR;
        }


        if (duration < 0.5) {
            ticket.setPrice(0.0);
        } else {
            System.out.println(ticket.getParkingSpot().getParkingType());
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR:
                if (discount) {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * Fare.DISCOUNT_RATE);
                } else {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                }
                break;

                case BIKE:
                if (discount) {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * Fare.DISCOUNT_RATE);
                    } else {
                        ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                        }
                break;
            
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
    }
}