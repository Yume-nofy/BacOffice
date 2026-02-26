package dao;

import model.Lieu;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LieuDAO {
    
    public void addLieu(Lieu lieu) {
        String sql = "INSERT INTO lieu (libelle, code) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, lieu.getLibelle());
            ps.setString(2, lieu.getCode());
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    lieu.setId(rs.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Lieu> getAllLieux() {
        List<Lieu> lieux = new ArrayList<>();
        String sql = "SELECT * FROM lieu ORDER BY id";
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Lieu l = new Lieu(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getString("code")
                );
                lieux.add(l);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lieux;
    }
    
    // Récupérer un lieu par ID
    public Lieu getLieuById(int id) {
        String sql = "SELECT * FROM lieu WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Lieu(
                            rs.getInt("id"),
                            rs.getString("libelle"),
                            rs.getString("code")
                    );
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public Lieu getLieuByCode(String code) {
        String sql = "SELECT * FROM lieu WHERE code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Lieu(
                            rs.getInt("id"),
                            rs.getString("libelle"),
                            rs.getString("code")
                    );
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}