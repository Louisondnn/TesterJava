package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import java.time.Duration;

        public class FareCalculatorService {
        
            public double calculateFare(Ticket ticket) {
                if (ticket == null || ticket.getInTime() == null || ticket.getOutTime() == null) {
                    throw new IllegalArgumentException("Ticket or its in/out time cannot be null");
                }
                if (ticket.getInTime().isAfter(ticket.getOutTime())) {
                    throw new IllegalArgumentException("In time cannot be greater than out time");
                }
        
                // Calculate the duration of parking
                Duration duration = Duration.between(ticket.getInTime(), ticket.getOutTime());
                long totalMinutes = duration.toMinutes();
        
                // Initialize fare
                double fare= 0;
                System.out.println("Total minutes: " + totalMinutes);
                if (totalMinutes >= 30) {
                    double ratePerHour = 0;
            
                    switch (ticket.getParkingSpot().getParkingType()) {
                        case CAR:
                            ratePerHour = Fare.CAR_RATE_PER_HOUR;
                            break;
                        case BIKE:
                            ratePerHour = Fare.BIKE_RATE_PER_HOUR;
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown parking type: " + ticket.getParkingSpot().getParkingType());
                    }
                    long hours = totalMinutes / 60;
                    long minutes = totalMinutes % 60;
            
                    fare = (hours * ratePerHour) + (minutes * (ratePerHour / 60.0)); // Charge for remaining minutes
                    System.out.println("Calculated fare: " + fare);
                    System.out.println("Rate per hour: " + ratePerHour);
                }
                
                ticket.setPrice(fare);
                return fare; 
            }            
        }
        