package dao;

import model.Reservation;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // Ajouter une réservation
    public void addReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (idclient, idhotel, nb_passager, date_arrivee) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, reservation.getIdClient());
            ps.setInt(2, reservation.getIdHotel());
            ps.setInt(3, reservation.getNbPassager());
            ps.setTimestamp(4, Timestamp.valueOf(reservation.getDateArrivee()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    reservation.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lister toutes les réservations
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation r join  hotel h on h.id=r.idhotel";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Reservation r = new Reservation(
                        rs.getInt("id"),
                        rs.getString("idclient"),
                        rs.getInt("idhotel"),
                        rs.getInt("nb_passager"),
                        rs.getTimestamp("date_arrivee").toLocalDateTime(),
                        rs.getString("nom")
                );
                reservations.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    // Chercher une réservation par id
    public Reservation getReservationById(int id) {
        String sql = "CT * FROM reservation r join  hotel h on h.id=r.idhotel WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Reservation(
                            rs.getInt("id"),
                            rs.getString("idclient"),
                            rs.getInt("idhotel"),
                            rs.getInt("nb_passager"),
                            rs.getTimestamp("date_arrivee").toLocalDateTime(),
                            rs.getString("nom")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Mettre à jour une réservation
    public void updateReservation(Reservation reservation) {
        String sql = "UPDATE reservation SET idclient=?, idhotel=?, nb_passager=?, date_arrivee=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservation.getIdClient());
            ps.setInt(2, reservation.getIdHotel());
            ps.setInt(3, reservation.getNbPassager());
            ps.setTimestamp(4, Timestamp.valueOf(reservation.getDateArrivee()));
            ps.setInt(5, reservation.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer une réservation
    public void deleteReservation(int id) {
        String sql = "DELETE FROM reservation WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
