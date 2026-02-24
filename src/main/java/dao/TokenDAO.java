package dao;

import model.Token;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TokenDAO {
    
    public void addToken(Token token) {
        String sql = "INSERT INTO token (token, date_expiration) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, token.getToken());
            ps.setTimestamp(2, Timestamp.valueOf(token.getDateExpiration()));
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    token.setId(rs.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Token> getAllTokens() {
        List<Token> tokens = new ArrayList<>();
        String sql = "SELECT * FROM token ORDER BY date_creation DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Token t = new Token(
                        rs.getInt("id"),
                        rs.getString("token"),
                        rs.getTimestamp("date_expiration").toLocalDateTime(),
                        rs.getTimestamp("date_creation").toLocalDateTime()
                );
                tokens.add(t);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tokens;
    }
    
    public Token getTokenById(int id) {
        String sql = "SELECT * FROM token WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Token(
                            rs.getInt("id"),
                            rs.getString("token"),
                            rs.getTimestamp("date_expiration").toLocalDateTime(),
                            rs.getTimestamp("date_creation").toLocalDateTime()
                    );
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void deleteToken(int id) {
        String sql = "DELETE FROM token WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void cleanExpiredTokens() {
        String sql = "DELETE FROM token WHERE date_expiration < ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}