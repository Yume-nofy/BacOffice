package dao;

import model.Trajet;
import model.Vehicule;
import model.Assignation;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

public class TrajetDAO {

    private VehiculeDAO vehiculeDAO = new VehiculeDAO();
    private AssignationDAO assignationDAO = new AssignationDAO();

    public int addTrajet(Trajet trajet) {
        String sql = "INSERT INTO trajet (idvehicule, distance_parcourue, date_depart, date_retour) VALUES (?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, trajet.getIdVehicule());
            ps.setDouble(2, trajet.getDistanceParcourue() != null ? trajet.getDistanceParcourue() : 0.0);
            ps.setTimestamp(3, Timestamp.valueOf(trajet.getDateDepart()));
            ps.setTimestamp(4, Timestamp.valueOf(trajet.getDateRetour()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    trajet.setId(generatedId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    public List<Trajet> getAllTrajets() {
        List<Trajet> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet ORDER BY date_depart DESC";

        try (Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Trajet t = extractTrajetFromResultSet(rs);
                trajets.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trajets;
    }

    /**
     * Get trajet by ID with its assignations
     */
    public Trajet getTrajetById(int id) {
        String sql = "SELECT * FROM trajet WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Trajet trajet = extractTrajetFromResultSet(rs);

                    // Load assignations
                    List<Assignation> assignations = assignationDAO.getAssignationsByTrajetId(id);
                    trajet.setAssignations(assignations);

                    // Load vehicule
                    Vehicule vehicule = vehiculeDAO.getVehiculeById(trajet.getIdVehicule());
                    trajet.setVehicule(vehicule);

                    return trajet;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get trajets by vehicule ID
     */
    public List<Trajet> getTrajetsByVehiculeId(int vehiculeId) {
        List<Trajet> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet WHERE idvehicule = ? ORDER BY date_depart DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehiculeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Trajet t = extractTrajetFromResultSet(rs);
                    trajets.add(t);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trajets;
    }

    /**
     * Get trajets by date range
     */
    public List<Trajet> getTrajetsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Trajet> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet WHERE date_depart BETWEEN ? AND ? ORDER BY date_depart";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Trajet t = extractTrajetFromResultSet(rs);
                    trajets.add(t);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trajets;
    }

    /**
     * Update a trajet
     */
    public void updateTrajet(Trajet trajet) {
        String sql = "UPDATE trajet SET idvehicule=?, distance_parcourue=?, date_depart=?, date_retour=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, trajet.getIdVehicule());
            ps.setDouble(2, trajet.getDistanceParcourue() != null ? trajet.getDistanceParcourue() : 0.0);
            ps.setTimestamp(3, Timestamp.valueOf(trajet.getDateDepart()));
            ps.setTimestamp(4, Timestamp.valueOf(trajet.getDateRetour()));
            ps.setInt(5, trajet.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a trajet and its assignations
     */
    public void deleteTrajet(int id) {
        // First delete assignations
        assignationDAO.deleteAssignationsByTrajetId(id);

        // Then delete trajet
        String sql = "DELETE FROM trajet WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to extract Trajet from ResultSet
     */
    private Trajet extractTrajetFromResultSet(ResultSet rs) throws SQLException {
        Trajet trajet = new Trajet(
                rs.getInt("id"),
                rs.getInt("idvehicule"),
                rs.getDouble("distance_parcourue"),
                rs.getTimestamp("date_depart").toLocalDateTime(),
                rs.getTimestamp("date_retour").toLocalDateTime());
        return trajet;
    }

    /**
     * Get the last trajet for a vehicule
     */
    public Trajet getLastTrajetByVehiculeId(int vehiculeId) {
        String sql = "SELECT * FROM trajet WHERE idvehicule = ? ORDER BY date_retour DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehiculeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractTrajetFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Vehicule> getVehiculesDisponibles(LocalDateTime dateDebut, LocalDateTime dateFin) {
        List<Vehicule> vehiculesDisponibles = new ArrayList<>();

        String sql = "SELECT v.*, vd.nombre_trajets, vd.derniere_date_retour " +
                "FROM vehicule v " +
                "JOIN vehicule_disponibilite vd ON v.id = vd.id " +
                "WHERE vd.derniere_date_retour <= ? " +
                "AND (vd.derniere_date_retour BETWEEN ? AND ? " +
                "OR vd.derniere_date_retour = TIMESTAMP '1970-01-01 00:00:00') " +
                "ORDER BY v.nbr_place ASC, " +
                "CASE v.type_carburant " +
                "    WHEN 'D' THEN 1 " +
                "    WHEN 'Es' THEN 2 " +
                "    WHEN 'El' THEN 3 " +
                "    ELSE 4 " +
                "END ASC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(dateDebut));
            ps.setTimestamp(2, Timestamp.valueOf(dateDebut));
            ps.setTimestamp(3, Timestamp.valueOf(dateFin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vehicule v = new Vehicule();
                    v.setId(rs.getInt("id"));
                    v.setReference(rs.getString("reference"));
                    v.setTypeCarburant(rs.getString("type_carburant"));
                    v.setNbrPlace(rs.getInt("nbr_place"));
                    v.setNombreTrajet(rs.getInt("nombre_trajets"));
                    v.setDateRetour(rs.getObject("derniere_date_retour", LocalDateTime.class));

                    vehiculesDisponibles.add(v);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehiculesDisponibles;
    }

    public void deleteAllTrajets(LocalDate dateDebut, LocalDate dateFin) {
        String sql = "DELETE FROM trajet WHERE date_depart BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(dateDebut.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(dateFin.atTime(23, 59, 59)));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}