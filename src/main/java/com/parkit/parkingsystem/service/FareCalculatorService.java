package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

        public class FareCalculatorService {
        
            public Map<String, Boolean> recurrentUsers; 
            public Map<String, Boolean> usersFromDatabase; 
            public static final double RECURRENT_USER_DISCOUNT = 0.95; // 5% de remise

              public FareCalculatorService() {
                this.recurrentUsers = new HashMap<>();
                this.usersFromDatabase = new HashMap<>();
            }
            public boolean isRecurrentUser (String licensePlate) {
                return recurrentUsers.containsKey(licensePlate) && recurrentUsers.get(licensePlate);
            }
            public Map<String, Boolean> getRecurrentUsers() {
                return recurrentUsers;
            }
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
            
                    fare = (hours * ratePerHour) + (minutes * (ratePerHour / 60.0));
                    System.out.println("Calculated fare: " + fare);
                    System.out.println("Rate per hour: " + ratePerHour);
                }
                if (ticket.isRecurrent()) {
                    fare *= RECURRENT_USER_DISCOUNT;
                    System.out.println("Remise de 5% appliquée pour l'utilisateur récurrent.");
                }
                ticket.setPrice(fare);
                return fare; 
            }            
            public void enterGarage(String licensePlate) {
                if (usersFromDatabase.containsKey(licensePlate)) {
                    System.out.println("Heureux de vous revoir ! En tant qu'utilisateur régulier de notre parking, vous allez obtenir une remise de 5%");
                    recurrentUsers.put(licensePlate, true); // Marquer comme récurrent
                    System.out.println("User  marked as recurrent: " + licensePlate);
                } else {
                    recurrentUsers.put(licensePlate, false); // Premier passage
                    System.out.println("Bienvenue dans notre garage !");
                }
            }
        
           
        
            public void exitGarage(String licensePlate, double normalTariff) {
                double finalTariff = calculateTariff(licensePlate, normalTariff); // Calculer le tarif final
                if (recurrentUsers.containsKey(licensePlate)) {
                    if (recurrentUsers.get(licensePlate)) {
                        System.out.println("Merci pour votre visite ! Vous avez obtenu une remise de 5% sur votre tarif.");
                    } else {
                        System.out.println("Merci pour votre visite !");
                    }
                } else {
                    System.out.println("Merci pour votre visite !");
                }
                System.out.println("Le tarif à payer est : " + finalTariff); // Afficher le tarif final
            }
             public double calculateTariff(String licensePlate, double normalTariff) {
                if (recurrentUsers.containsKey(licensePlate) && recurrentUsers.get(licensePlate)) {
                    return normalTariff * RECURRENT_USER_DISCOUNT; // 5% remise pour les utilisateurs récurrents
                } else {
                    return normalTariff; // Pas de remise pour les nouveaux utilisateurs
                }
            }

        }
           
             