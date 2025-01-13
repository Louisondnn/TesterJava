package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;



public class ParkingServiceTest {

    private ParkingService parkingService;

    @Mock
    private InputReaderUtil inputReaderUtil;

    @Mock
    private ParkingSpotDAO parkingSpotDAO;

    @Mock
    private TicketDAO ticketDAO;

    @BeforeEach
    void setUp() {
        inputReaderUtil = mock(InputReaderUtil.class);
        parkingSpotDAO = mock(ParkingSpotDAO.class);
        ticketDAO = mock(TicketDAO.class);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }

    @Test
    void testProcessIncomingVehicle() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABC123");
        when(inputReaderUtil.readSelection()).thenReturn(1); // CAR
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class)); // Ajouter cette ligne pour vérifier l'appel à updateParking
    }


 @Test
void testProcessExitingVehicle() throws Exception {
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABC123");

    Ticket ticket = new Ticket(null, null, 0);
    ticket.setVehicleRegNumber("ABC123");
    ticket.setInTime(LocalDateTime.now().minusHours(2));
    ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));

    when(ticketDAO.getTicket("ABC123")).thenReturn(ticket);
    when(ticketDAO.updateTicket(ticket)).thenReturn(true);

    parkingService.processExitingVehicle();

    // 
    verify(ticketDAO, times(1)).getTicket("ABC123");
    verify(ticketDAO, times(1)).updateTicket(ticket);
    verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
}
@Test
void testGetNextParkingNumberIfAvailable() throws Exception {

    when(inputReaderUtil.readSelection()).thenReturn(1); // CAR
    when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

    ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();

    assertNotNull(parkingSpot);
    equals(1);
    }
}
