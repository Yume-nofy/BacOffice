package model;

import java.time.LocalDateTime;

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

    public Reservation(int id, String idClient, int idHotel, int nbPassager, LocalDateTime dateArrivee,String nomHotel) {
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

}
