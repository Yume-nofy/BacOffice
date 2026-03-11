package dao;

import model.Reservation;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // Ajouter une reservation
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

    // Lister toutes les reservations
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation r join  lieu h on h.id=r.idhotel";

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
                        rs.getString("libelle")
                );
                reservations.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    public List<Reservation> getReservationsByDate(LocalDate dateDebut, LocalDate dateFin) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.id, r.idclient, r.idhotel, r.nb_passager, r.date_arrivee, h.libelle as hotel_nom " +
                    "FROM reservation r " +
                    "JOIN lieu h ON h.id = r.idhotel " +
                    "WHERE r.date_arrivee BETWEEN ? AND ? " +
                    "ORDER BY r.idhotel ASC ,r.date_arrivee ASC, r.nb_passager DESC";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            LocalDateTime debut = dateDebut.atStartOfDay();
            LocalDateTime fin = dateFin.atTime(23, 59, 59);
            
            ps.setTimestamp(1, Timestamp.valueOf(debut));
            ps.setTimestamp(2, Timestamp.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reservation r = new Reservation(
                        rs.getInt("id"),
                        rs.getString("idclient"),
                        rs.getInt("idhotel"),
                        rs.getInt("nb_passager"),
                        rs.getTimestamp("date_arrivee").toLocalDateTime(),
                        rs.getString("hotel_nom") 
                    );
                    reservations.add(r);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public Reservation getReservationById(int id) {
        String sql = "CT * FROM reservation r join  lieu h on h.id=r.idhotel WHERE id = ?";
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
                            rs.getString("libelle")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Mettre à jour une reservation
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

    // Supprimer une reservation
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
