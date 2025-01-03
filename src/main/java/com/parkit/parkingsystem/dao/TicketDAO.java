package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TicketDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    @SuppressWarnings("finally")
    public boolean saveTicket(Ticket ticket){
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            //ps.setInt(1,ticket.getId());
            System.out.println("Saving ticket with inTime: " + ticket.getInTime() + " and outTime: " + ticket.getOutTime());
            System.out.println("Parking Spot ID: " + ticket.getParkingSpot().getId());
            System.out.println("Vehicle Registration Number: " + ticket.getVehicleRegNumber());
            System.out.println("Price: " + ticket.getPrice());
    
            ps.setInt(1,ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
           // Utilisez setTimestamp à la place de setInTime
           ps.setTimestamp(4, Timestamp.valueOf(ticket.getInTime())); // Assurez-vous que getInTime() renvoie un LocalDateTime valide
           ps.setTimestamp(5, null); // ou une autre valeur si vous avez une date de sortie
           int affectedRows = ps.executeUpdate();
           return affectedRows > 0; // Retourne true si l'insertion a réussi

            // return ps.execute();
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
            return false;
        }
    }

    @SuppressWarnings("finally")
    public Ticket getTicket(String vehicleRegNumber) {
        Connection con = null;
        Ticket ticket = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1,vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ticket = new Ticket(vehicleRegNumber, null, 0);
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4).toLocalDateTime()); // Conversion de Timestamp à LocalDateTime
                ticket.setOutTime(rs.getTimestamp(5) != null ? rs.getTimestamp(5).toLocalDateTime() : null); // Gérer le cas où outTime est null
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
            return ticket;
        }
    }

    public boolean updateTicket(Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean isUpdated = false; // Variable pour suivre l'état de la mise à jour
    
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
    
            // Vérifiez si outTime est null et convertissez-le en Timestamp
            if (ticket.getOutTime() != null) {
                ps.setTimestamp(2, Timestamp.valueOf(ticket.getOutTime())); // Conversion de LocalDateTime à Timestamp
            } else {
                ps.setNull(2, java.sql.Types.TIMESTAMP); // Si outTime est null, définir le champ comme NULL dans la base de données
            }
    
            ps.setInt(3, ticket.getId());
    
            // Utilisez executeUpdate pour les mises à jour
            int rowsAffected = ps.executeUpdate();
            isUpdated = (rowsAffected > 0); // Vérifiez si la mise à jour a réussi
        } catch (Exception ex) {
            logger.error("Error updating ticket", ex);
        } finally {
            // Fermez les ressources dans l'ordre inverse de leur ouverture
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error("Error closing PreparedStatement", e);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    logger.error("Error closing Connection", e);
                }
            }
        }
        
        return isUpdated; // Retournez true si la mise à jour a réussi, sinon false
    }
    public void deleteTicket(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteTicket'");
    }
}
