package dao;

import model.Vehicule;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculeDAO {
    
    public void addVehicule(Vehicule vehicule) {
        String sql = "INSERT INTO vehicule (reference, type_carburant, nbr_place) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, vehicule.getReference());
            ps.setString(2, vehicule.getTypeCarburant());
            ps.setInt(3, vehicule.getNbrPlace());
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    vehicule.setId(rs.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Vehicule> getAllVehicules() {
        List<Vehicule> vehicules = new ArrayList<>();
        String sql = "SELECT * FROM vehicule\r\n" + //
                        "ORDER BY \r\n" + //
                        "    nbr_place ASC,\r\n" + //
                        "    CASE type_carburant\r\n" + //
                        "        WHEN 'D' THEN 1\r\n" + //
                        "        WHEN 'Es' THEN 2\r\n" + //
                        "        WHEN 'El' THEN 3\r\n" + //
                        "        ELSE 4\r\n" + //
                        "    END ASC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Vehicule v = new Vehicule(
                        rs.getInt("id"),
                        rs.getString("reference"),
                        rs.getString("type_carburant"),
                        rs.getInt("nbr_place")
                );
                vehicules.add(v);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return vehicules;
    }
    
    public Vehicule getVehiculeById(int id) {
        String sql = "SELECT * FROM vehicule WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Vehicule(
                            rs.getInt("id"),
                            rs.getString("reference"),
                            rs.getString("type_carburant"),
                            rs.getInt("nbr_place")
                    );
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void updateVehicule(Vehicule vehicule) {
        String sql = "UPDATE vehicule SET reference=?, type_carburant=?, nbr_place=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, vehicule.getReference());
            ps.setString(2, vehicule.getTypeCarburant());
            ps.setInt(3, vehicule.getNbrPlace());
            ps.setInt(4, vehicule.getId());
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteVehicule(int id) {
        String sql = "DELETE FROM vehicule WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}