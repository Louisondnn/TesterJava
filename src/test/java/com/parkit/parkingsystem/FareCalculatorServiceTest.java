package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FareCalculatorServiceTest {

    private final FareCalculatorService fareCalculatorService = new FareCalculatorService();

    @Test
    void testCalculateFare_CarLessThan30Minutes() {

        Ticket ticket = new Ticket(null, null, 0);
        ParkingSpot spot = new ParkingSpot(0, ParkingType.CAR, false);
        ticket.setParkingSpot(spot);
        // ticket.setInTime(new Date(System.currentTimeMillis()));
        // ticket.setOutTime(new Date(System.currentTimeMillis() + 15 * 60 * 1000)); // 15 minutes later
        LocalDateTime inTime = LocalDateTime.now(); 
        ticket.setInTime(inTime); // Use LocalDateTime
    
    // Set outTime to 15 minutes later
        LocalDateTime outTime = inTime.plus(15, ChronoUnit.MINUTES); // Add 15 minutes
        ticket.setOutTime(outTime); // Use LocalDateTime


        double fare = fareCalculatorService.calculateFare(ticket);


        assertEquals(0, fare, 0.01);
    }

    @Test
    void testCalculateFare_CarMoreThan30Minutes() {

        Ticket ticket = new Ticket(null, null, 0);
        ParkingSpot spot = new ParkingSpot(0, ParkingType.CAR, false);
        ticket.setParkingSpot(spot);
        LocalDateTime inTime = LocalDateTime.now(); 
        ticket.setInTime(inTime); 
        
        LocalDateTime outTime = inTime.plus(2, ChronoUnit.HOURS);
        ticket.setOutTime(outTime);

        double fare = fareCalculatorService.calculateFare(ticket);

        double expectedFare = 2 * Fare.CAR_RATE_PER_HOUR; 
        assertEquals(expectedFare, fare, 3);
    }
    @Test
    void testCalculateFare_NullTicket() {
        assertThrows(IllegalArgumentException.class, () -> {
            fareCalculatorService.calculateFare(null);
        });
    }
    // @Test
    // void testCalculateFare_NullInTime() {
    //     Ticket ticket = new Ticket("ABC123", new ParkingSpot(1, ParkingType.CAR, false), 0);
    //     ticket.setInTime(null);
    //     ticket.setOutTime(LocalDateTime.now());
    //     assertThrows(IllegalArgumentException.class, () -> {
    //         fareCalculatorService.calculateFare(ticket);
    //     });
    //  }    
    // @Test
    // void testCalculateFare_NullOutTime() {
    //     Ticket ticket = new Ticket("ABC123", new ParkingSpot(1, ParkingType.CAR, false), 0);
    //     ticket.setInTime(LocalDateTime.now());
    //     ticket.setOutTime(null);
    //     assertThrows(IllegalArgumentException.class, () -> {
    //         fareCalculatorService.calculateFare(ticket);
    //         });
    //     }
    @Test
    void testCalculateFare_InTimeAfterOutTime() {
        Ticket ticket = new Ticket("ABC123", new ParkingSpot(1, ParkingType.CAR, false), 0);
         ticket.setInTime(LocalDateTime.now());
        ticket.setOutTime(LocalDateTime.now().minusMinutes(30));
        assertThrows(IllegalArgumentException.class, () -> {
            fareCalculatorService.calculateFare(ticket);
            });
        }
        @Test
        void testCalculateFare_LessThan30Minutes() {
            Ticket ticket = new Ticket("TEST123", new ParkingSpot(1, ParkingType.CAR, false), 0);
            ticket.setInTime(LocalDateTime.now().minusMinutes(20));
            ticket.setOutTime(LocalDateTime.now());
            
            double fare = fareCalculatorService.calculateFare(ticket);
            assertEquals(0, fare);
        }
      

}



