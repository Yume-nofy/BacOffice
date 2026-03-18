package model;

public class Assignation {
    private int id;
    private int idTrajet;
    private int idReservation;
    private int ordre;
    private int nbpassager;
    
    // Relationships
    private Trajet trajet;
    private Reservation reservation;
    
    public Assignation() {}
    
    public Assignation(int idTrajet, int idReservation, int ordre) {
        this.idTrajet = idTrajet;
        this.idReservation = idReservation;
        this.ordre = ordre;
    }
    
    public Assignation(int id, int idTrajet, int idReservation, int ordre, int nbpassger) {
        this.id = id;
        this.idTrajet = idTrajet;
        this.idReservation = idReservation;
        this.ordre = ordre;
        this.nbpassager= nbpassger;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdTrajet() {
        return idTrajet;
    }
    
    public void setIdTrajet(int idTrajet) {
        this.idTrajet = idTrajet;
    }
    
    public int getIdReservation() {
        return idReservation;
    }
    
    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }
    
    public int getOrdre() {
        return ordre;
    }
    
    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }
    
    public Trajet getTrajet() {
        return trajet;
    }
    
    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
        if (trajet != null) {
            this.idTrajet = trajet.getId();
        }
    }
    
    public Reservation getReservation() {
        return reservation;
    }
    
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        if (reservation != null) {
            this.idReservation = reservation.getId();
        }
    }

    public int getNbpassager() {
        return nbpassager;
    }

    public void setNbpassager(int nbpassager) {
        this.nbpassager = nbpassager;
    }
    
    @Override
    public String toString() {
        return "Assignation{" +
                "id=" + id +
                ", idTrajet=" + idTrajet +
                ", idReservation=" + idReservation +
                ", ordre=" + ordre +
                ", nb_passager " + nbpassager +
                '}';
    }
}