package model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Reservation {

    private int id;
    private String idClient;
    private int idHotel;
    private int nbPassager;
    private LocalDateTime dateArrivee;
    private String nomHotel; // Ajoute pour stocker le nom de l'hôtel
    // Constructeurs

    public Reservation() {
    }

    public Reservation(String idClient, int idHotel, int nbPassager, LocalDateTime dateArrivee) {
        this.idClient = idClient;
        this.idHotel = idHotel;
        this.nbPassager = nbPassager;
        this.dateArrivee = dateArrivee;
    }

    public Reservation(int id, String idClient, int idHotel, int nbPassager, LocalDateTime dateArrivee,
            String nomHotel) {
        this.id = id;
        this.idClient = idClient;
        this.idHotel = idHotel;
        this.nbPassager = nbPassager;
        this.dateArrivee = dateArrivee;
        this.nomHotel = nomHotel;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdClient() {
        return idClient;
    }

    public String getNomHotel() {
        return nomHotel;
    }

    public void setNomHotel(String nomHotel) {
        this.nomHotel = nomHotel;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public int getNbPassager() {
        return nbPassager;
    }

    public void setNbPassager(int nbPassager) {
        this.nbPassager = nbPassager;
    }

    public LocalDateTime getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", idClient=" + idClient +
                ", idHotel=" + idHotel +
                ", nbPassager=" + nbPassager +
                ", dateArrivee=" + dateArrivee +
                ", nomHotel='" + nomHotel + '\'' +
                '}';
    }

    public boolean memeReservation(Reservation r) {
        if (r == null) {
            return false;
        }
        return this.dateArrivee.truncatedTo(ChronoUnit.MINUTES)
                .equals(r.getDateArrivee().truncatedTo(ChronoUnit.MINUTES));
    }

    public Vehicule getVehiculeApproprie(List<Vehicule> vehicules, LocalDateTime dateComp, LocalDateTime dateFin) {
        List<Vehicule> meilleurChoix = new ArrayList<>();
        LocalDateTime epoch = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        for (Vehicule v : vehicules) {

            boolean AssignExiste = false;
            if (v.getReservationsAssign() != null && !v.getReservationsAssign().isEmpty()) {
                AssignExiste = true;
            }
            // System.out.println("Véhicule " + v.getId() + " - Date retour : "
                    // + (v.getDateRetour() == null ? "null" : v.getDateRetour()));
            // System.out.println("Comparaison dates : " + dateComp + " < " + v.getDateRetour() + " < " + dateFin);

            if (v.getNbrPlaceDisponible() >= this.nbPassager && ((v.getDateRetour().isBefore(dateFin) && v.getDateRetour().isAfter(dateComp)) || v.getDateRetour().isEqual(epoch))) {
                if (v.getReservationsAssign() == null || v.getReservationsAssign().isEmpty()
                        || v.getReservationsAssign().get(0).getDateArrivee().truncatedTo(ChronoUnit.MINUTES)
                                .equals(this.dateArrivee.truncatedTo(ChronoUnit.MINUTES))) {
                    if (meilleurChoix == null || meilleurChoix.isEmpty()) {
                        System.out.println("Premier choix : " + v);
                        meilleurChoix.add(v);
                    } else if (v.getNbrPlaceDisponible() < meilleurChoix.get(0).getNbrPlaceDisponible()
                            && AssignExiste) {
                        meilleurChoix.clear();
                        System.out.println("Deuxieme choix trouvé : " + v);
                        meilleurChoix.add(v);

                    } else if (v.getNbrPlaceDisponible() == meilleurChoix.get(0).getNbrPlaceDisponible()
                            && AssignExiste) {

                        if (v.getNombreTrajet() < meilleurChoix.get(0).getNombreTrajet()) {
                            meilleurChoix.clear();
                            System.out.println("Troisième choix trouvé : " + v);
                            meilleurChoix.add(v);
                        } else if (v.getNombreTrajet() == meilleurChoix.get(0).getNombreTrajet()) {
                            if (getPrioriteCarburant(v.getTypeCarburant()) < getPrioriteCarburant(
                                    meilleurChoix.get(0).getTypeCarburant())) {
                                meilleurChoix.clear();
                                System.out.println("Quatrième choix trouvé : " + v);
                                meilleurChoix.add(v);
                            } else if (getPrioriteCarburant(v.getTypeCarburant()) == getPrioriteCarburant(
                                    meilleurChoix.get(0).getTypeCarburant())) {
                                System.out.println("Cinquième choix trouvé : " + v);    
                                meilleurChoix.add(v);
                            }
                        }
                    }

                }

            }

        }
        if (meilleurChoix.isEmpty()) {
            return null;
        } else if (meilleurChoix.size() == 1) {

            return meilleurChoix.get(0);

        } else {
            System.out.println("mety");
            Collections.shuffle(meilleurChoix);
            return meilleurChoix.get(0);
        }
    }

    private int getPrioriteCarburant(String type) {
        if (type == null)
            return 4;
        switch (type) {
            case "D":
                return 1; // Diesel (Priorité 1)
            case "Es":
                return 2; // Essence
            case "El":
                return 3; // Electrique
            default:
                return 4;
        }

    }
}
