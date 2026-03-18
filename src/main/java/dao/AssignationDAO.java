package dao;

import model.Assignation;
import model.Reservation;
import model.Trajet;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignationDAO {
    
    private ReservationDAO reservationDAO = new ReservationDAO();
   
    
    /**
     * Add a new assignation
     */
    public void addAssignation(Assignation assignation) {
        String sql = "INSERT INTO assignation (idtrajet, idreservation, ordre) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, assignation.getIdTrajet());
            ps.setInt(2, assignation.getIdReservation());
            ps.setInt(3, assignation.getOrdre());
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    assignation.setId(rs.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get all assignations
     */
    public List<Assignation> getAllAssignations() {
        List<Assignation> assignations = new ArrayList<>();
        String sql = "SELECT * FROM assignation ORDER BY idtrajet, ordre";
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Assignation a = extractAssignationFromResultSet(rs);
                assignations.add(a);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return assignations;
    }
    
    /**
     * Get assignation by ID
     */
    public Assignation getAssignationById(int id) {
        String sql = "SELECT * FROM assignation WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractAssignationFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get assignations by trajet ID
     */
    public List<Assignation> getAssignationsByTrajetId(int trajetId) {
        List<Assignation> assignations = new ArrayList<>();
        String sql = "SELECT * FROM assignation WHERE idtrajet = ? ORDER BY ordre";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, trajetId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Assignation a = extractAssignationFromResultSet(rs);
                    
                    // Load associated reservation
                    Reservation reservation = reservationDAO.getReservationById(a.getIdReservation());
                    a.setReservation(reservation);
                    
                    assignations.add(a);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return assignations;
    }
    
    /**
     * Get assignations by reservation ID
     */
    public List<Assignation> getAssignationsByReservationId(int reservationId) {
        List<Assignation> assignations = new ArrayList<>();
        String sql = "SELECT * FROM assignation WHERE idreservation = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, reservationId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Assignation a = extractAssignationFromResultSet(rs);
                    
                    // Load associated trajet
                    
                
                    
                    assignations.add(a);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return assignations;
    }
    
    /**
     * Get the maximum order for a trajet
     */
    public int getMaxOrderForTrajet(int trajetId) {
        String sql = "SELECT COALESCE(MAX(ordre), 0) FROM assignation WHERE idtrajet = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, trajetId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Update an assignation
     */
    public void updateAssignation(Assignation assignation) {
        String sql = "UPDATE assignation SET idtrajet=?, idreservation=?, ordre=? WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, assignation.getIdTrajet());
            ps.setInt(2, assignation.getIdReservation());
            ps.setInt(3, assignation.getOrdre());
            ps.setInt(4, assignation.getId());
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Delete an assignation
     */
    public void deleteAssignation(int id) {
        String sql = "DELETE FROM assignation WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Delete all assignations for a trajet
     */
    public void deleteAssignationsByTrajetId(int trajetId) {
        String sql = "DELETE FROM assignation WHERE idtrajet=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, trajetId);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Helper method to extract Assignation from ResultSet
     */
    private Assignation extractAssignationFromResultSet(ResultSet rs) throws SQLException {
        Assignation assignation = new Assignation(
            rs.getInt("id"),
            rs.getInt("idtrajet"),
            rs.getInt("idreservation"),
            rs.getInt("ordre")
        );
        return assignation;
    }
    
    /**
     * Check if a reservation is already assigned to a trajet
     */
    public boolean isReservationAssigned(int reservationId) {
        String sql = "SELECT COUNT(*) FROM assignation WHERE idreservation = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, reservationId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}