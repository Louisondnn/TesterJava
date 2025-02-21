package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.mockito.Mockito.when;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;


@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1); // Mock vehicle type as CAR
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries(); // Reset database state before each test
    }

    @AfterAll
    private static void tearDown() {

    }
        //


    @Test
    public void testParkingService() throws Exception {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        ParkingSpot parkingSpot = parkingSpotDAO.getParkingSpot(1); //parking spot 1
        assertNotNull(parkingSpot);
        assertFalse(parkingSpot.isAvailable());

        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        assertNotNull(ticket);
        assertEquals("ABCDEF", ticket.getVehicleRegNumber());
        assertNotNull(ticket.getInTime());
        assertNull(ticket.getOutTime());
        assertEquals(0, ticket.getPrice(), "Price should be 0 for incoming vehicle");
    }

    @Test
    public void testParkingLotExit() throws Exception{
        testParkingService(); 
        
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();

        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        assertNotNull(ticket);
        
        assertFalse(ticket.getPrice() > 0, "The parking fare should be calculated and set");

        ParkingSpot parkingSpot = parkingSpotDAO.getParkingSpot(1); 
        assertNotNull(parkingSpot);
        assertTrue(parkingSpot.isAvailable(), "The parking spot should be marked as available after the car exits");
    }
        @Test
        public void testParkingLotExitRecurringUser() throws Exception {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle(); 
        parkingService.processExitingVehicle(); 
        parkingService.processIncomingVehicle(); 
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        if (ticket == null) {
            fail("Ticket with registration number 'ABCDEF' not found");
        }

        assertEquals("ABCDEF", ticket.getVehicleRegNumber());
        assertEquals(ParkingType.CAR, ticket.getParkingSpot().getParkingType());

        Date inTime = new Date(0, 0, 0);
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // 24h
        ticket.setInTime(inTime);
        ticketDAO.saveTicket(ticket);

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingService.processExitingVehicle();
        Ticket ticketExiting = ticketDAO.getTicket("ABCDEF");
        
        assertNotNull(ticketExiting.getOutTime());
    }
    }