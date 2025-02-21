package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.BooleanSupplier;

public class ParkingSpotDAO {
    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    
    public int getNextAvailableSlot(ParkingType parkingType){
        Connection con = null;
        int result=-1;
        System.out.println("pk" + parkingType);
        
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            ResultSet rs = ps.executeQuery();
            System.out.println("rs" + rs);
            if(rs.next()){
                result = rs.getInt(1);;
            }
            
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return result;
    }
    public boolean updateParking(ParkingSpot parkingSpot){
        //update the availability fo that parking slot
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            int updateRowCount = ps.executeUpdate();
            dataBaseConfig.closePreparedStatement(ps);
            return (updateRowCount == 1);
        }catch (Exception ex){
            logger.error("Error updating parking info",ex);
            return false;
        }finally {
            dataBaseConfig.closeConnection(con);
        }
    }

    public ParkingSpot freeParkingSpot(int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'freeParkingSpot'");
    }

    public Object getAvailableSpot() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAvailableSpot'");
    }

    public BooleanSupplier isSpotAvailable(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isSpotAvailable'");
    }
    public Object findAvailableParkingSpot() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAvailableParkingSpot'");
    }
    public ParkingSpot getParkingSpot(int parkingNumber) {
        ParkingSpot parkingSpot = null;
        Connection con = null;
    
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_PARKING_SPOT_BY_NUMBER);
            ps.setInt(1, parkingNumber); // Set the parking spot number as the parameter
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("PARKING_NUMBER");
                ParkingType type = ParkingType.valueOf(rs.getString("TYPE")); // Assuming TYPE column is a string representation of ParkingType
                boolean available = rs.getBoolean("AVAILABLE");
                parkingSpot = new ParkingSpot(id, type, available); // Create ParkingSpot object based on DB data
            }
    
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception e) {
            logger.error("Error fetching parking spot", e);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        
        return parkingSpot;
    }
    

}
