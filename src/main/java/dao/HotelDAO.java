package dao;

import model.Hotel;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {

    // Ajouter un hôtel
    public void addHotel(Hotel hotel) {
        String sql = "INSERT INTO lieu (libelle) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, hotel.getNom());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    hotel.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lister tous les hôtels
    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM lieu";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Hotel h = new Hotel(rs.getInt("id"), rs.getString("libelle"));
                hotels.add(h);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotels;
    }

    // Chercher un hôtel par id
    public Hotel getHotelById(int id) {
        String sql = "SELECT * FROM lieu WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Hotel(rs.getInt("id"), rs.getString("libelle"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Mettre à jour un hôtel
    public void updateHotel(Hotel hotel) {
        String sql = "UPDATE lieu SET libelle = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hotel.getNom());
            ps.setInt(2, hotel.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un hôtel
    public void deleteHotel(int id) {
        String sql = "DELETE FROM lieu WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
