package dao;

import model.Distance;
import model.Lieu;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DistanceDAO {
    
    private final LieuDAO lieuDAO = new LieuDAO();
    
    public void addDistance(Distance distance) {
        String sql = "INSERT INTO distance (from_lieu_id, to_lieu_id, kilometer) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, distance.getFromLieu().getId());
            ps.setInt(2, distance.getToLieu().getId());
            ps.setBigDecimal(3, distance.getKilometer());
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    distance.setId(rs.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Distance> getAllDistances() {
        List<Distance> distances = new ArrayList<>();
        String sql = "SELECT * FROM distance ORDER BY from_lieu_id, to_lieu_id";
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Lieu fromLieu = lieuDAO.getLieuById(rs.getInt("from_lieu_id"));
                Lieu toLieu = lieuDAO.getLieuById(rs.getInt("to_lieu_id"));
                
                Distance d = new Distance(
                        rs.getInt("id"),
                        fromLieu,
                        toLieu,
                        rs.getBigDecimal("kilometer")
                );
                distances.add(d);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return distances;
    }
    
    public List<Distance> getDistancesFromLieu(int lieuId) {
        List<Distance> distances = new ArrayList<>();
        String sql = "SELECT * FROM distance WHERE from_lieu_id = ? ORDER BY to_lieu_id";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, lieuId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lieu fromLieu = lieuDAO.getLieuById(lieuId);
                    Lieu toLieu = lieuDAO.getLieuById(rs.getInt("to_lieu_id"));
                    
                    Distance d = new Distance(
                            rs.getInt("id"),
                            fromLieu,
                            toLieu,
                            rs.getBigDecimal("kilometer")
                    );
                    distances.add(d);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return distances;
    }
    
    public Distance getDistanceBetween(int idFrom, int idTo) {
        String sql = "SELECT * FROM distance WHERE (from_lieu_id = ? AND to_lieu_id = ?) or (to_lieu_id = ? AND from_lieu_id = ?)";
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idFrom);
            ps.setInt(2, idTo);
            ps.setInt(3, idFrom);
            ps.setInt(4, idTo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Lieu fromLieu = lieuDAO.getLieuById(idFrom);
                    Lieu toLieu = lieuDAO.getLieuById(idTo);
                    
                    return new Distance(
                            rs.getInt("id"),
                            fromLieu,
                            toLieu,
                            rs.getBigDecimal("kilometer")
                    );
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}