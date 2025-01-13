package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FareCalculatorServiceTest {

    private FareCalculatorService fareCalculatorService = new FareCalculatorService();
    
    
        @Mock
        private Ticket ticket;
    
        @Mock
        private ParkingSpot parkingSpot;
    
          @BeforeEach
        public void setUp() {
            fareCalculatorService = new FareCalculatorService();

    }

     @Test
    public void calculateFare_Car_RecurrentUser () {
        LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusHours(2);
        LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());

        when(ticket.getInTime()).thenReturn(inTime);
        when(ticket.getOutTime()).thenReturn(outTime);
        when(ticket.getParkingSpot()).thenReturn(parkingSpot);
        when(parkingSpot.getParkingType()).thenReturn(ParkingType.CAR);
        when(ticket.getLicensePlate()).thenReturn("recurrentLicensePlate");
        fareCalculatorService.mapRecurrentUsers(Collections.singletonList(ticket));

        double fare = fareCalculatorService.calculateFare(ticket);

        // Assert
        double expectedFare = 3.0;
        assertEquals(expectedFare, fare, 0.001);
    }

    @Test
    public void calculateFare_Bike_NonRecurrentUser() {
        LocalDateTime inTime = LocalDateTime.now(ZoneId.systemDefault()).minusHours(1);
        LocalDateTime outTime = LocalDateTime.now(ZoneId.systemDefault());

        when(ticket.getInTime()).thenReturn(inTime);
        when(ticket.getOutTime()).thenReturn(outTime);
        when(ticket.getParkingSpot()).thenReturn(parkingSpot);
        when(parkingSpot.getParkingType()).thenReturn(ParkingType.BIKE);
        when(ticket.getLicensePlate()).thenReturn("nonRecurrentLicensePlate");

        fareCalculatorService.mapRecurrentUsers(Collections.emptyList());

        double fare = fareCalculatorService.calculateFare(ticket);

        double expectedFare = 1 * Fare.BIKE_RATE_PER_HOUR;
        assertEquals(expectedFare, fare, 0.01);
    }

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
    @Test
    void testCalculateFare_NullInTime() {
        Ticket ticket = new Ticket("ABC123", new ParkingSpot(1, ParkingType.CAR, false), 0);
        ticket.setInTime(null);
        ticket.setOutTime(LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> {
            fareCalculatorService.calculateFare(ticket);
        });
     }    
    @Test
    void testCalculateFare_NullOutTime() {
        Ticket ticket = new Ticket("ABC123", new ParkingSpot(1, ParkingType.CAR, false), 0);
        ticket.setInTime(LocalDateTime.now());
        ticket.setOutTime(null);
        assertThrows(IllegalArgumentException.class, () -> {
            fareCalculatorService.calculateFare(ticket);
            });
        }
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
        @Test
        public void testEnterGarage_RecurrentUser () {
            String licensePlate = "ABC123";
            when(ticket.getLicensePlate()).thenReturn(licensePlate);
            fareCalculatorService.usersFromDatabase.put(licensePlate, true);
        
            fareCalculatorService.enterGarage(ticket.getLicensePlate());
        
            assertTrue(fareCalculatorService.getRecurrentUsers().containsKey(licensePlate));
            assertTrue(fareCalculatorService.getRecurrentUsers().get(licensePlate));        
        }
        @Test
        public void testEnterGarage_NewUser  () {
            String licensePlate = "DEF456";            
            fareCalculatorService.enterGarage(licensePlate);
            
            assertTrue(fareCalculatorService.getRecurrentUsers().containsKey(licensePlate));
            assertTrue(fareCalculatorService.getRecurrentUsers().get(licensePlate));        
            assertTrue(fareCalculatorService.usersFromDatabase.containsKey(licensePlate));
        }
            
        @Test
        public void testExitGarage_RecurrentUser () {
            String licensePlate = "ABC123";
            fareCalculatorService.usersFromDatabase.put(licensePlate, true);
            fareCalculatorService.recurrentUsers.put(licensePlate, true);
            double normalTariff = 10.0;

            fareCalculatorService.exitGarage(licensePlate, normalTariff);

            assertTrue(fareCalculatorService.recurrentUsers.containsKey(licensePlate));
            assertTrue(fareCalculatorService.recurrentUsers.get(licensePlate));
        }
        @Test
        public void testCalculateTariff_RecurrentUser () {
            String licensePlate = "ABC123";
            fareCalculatorService.recurrentUsers.put(licensePlate, true);
            double normalTariff = 10.0;

            double finalTariff = fareCalculatorService.calculateTariff(licensePlate, normalTariff);

            assertEquals(normalTariff * FareCalculatorService.RECURRENT_USER_DISCOUNT, finalTariff, 0.01);
        }
        @Test
        public void testExitGarage_NewUser   () {
            String licensePlate = "DEF456";
            fareCalculatorService.recurrentUsers.put(licensePlate, false);
            double normalTariff = 10.0;

            fareCalculatorService.exitGarage(licensePlate, normalTariff);

            assertTrue(fareCalculatorService.recurrentUsers.containsKey(licensePlate));
            assertFalse(fareCalculatorService.recurrentUsers.get(licensePlate));
        }
        @Test
    public void mapRecurrentUsers_RecurrentUser () {
        Ticket ticket = mock(Ticket.class);
        when(ticket.getLicensePlate()).thenReturn("existentLicensePlate");
        when(ticket.getOutTime()).thenReturn(LocalDateTime.now());

        fareCalculatorService.usersFromDatabase.put("existentLicensePlate", true);
        fareCalculatorService.mapRecurrentUsers(Collections.singletonList(ticket));

        assertTrue(fareCalculatorService.recurrentUsers.get("existentLicensePlate"));
}

    @Test
    public void mapRecurrentUsers_NoOutTime() {
        Ticket ticket = mock(Ticket.class);
        when(ticket.getLicensePlate()).thenReturn("existentLicensePlate");
        when(ticket.getOutTime()).thenReturn(null);

        fareCalculatorService.usersFromDatabase.put("existentLicensePlate", true);

        fareCalculatorService.mapRecurrentUsers(Collections.singletonList(ticket));

        assertFalse(fareCalculatorService.recurrentUsers.get("existentLicensePlate"));
    }
                
}



