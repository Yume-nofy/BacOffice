package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Trajet {
    private int id;
    private int idVehicule;
    private Double distanceParcourue;
    private LocalDateTime dateDepart;
    private LocalDateTime dateRetour;
    
    // Relationships
    private Vehicule vehicule;
    private List<Assignation> assignations;
    
    public Trajet() {
        this.assignations = new ArrayList<>();
    }
    
    public Trajet(int idVehicule, Double distanceParcourue, LocalDateTime dateDepart, LocalDateTime dateRetour) {
        this.idVehicule = idVehicule;
        this.distanceParcourue = distanceParcourue;
        this.dateDepart = dateDepart;
        this.dateRetour = dateRetour;
        this.assignations = new ArrayList<>();
    }
    
    public Trajet(int id, int idVehicule, Double distanceParcourue, LocalDateTime dateDepart, LocalDateTime dateRetour) {
        this.id = id;
        this.idVehicule = idVehicule;
        this.distanceParcourue = distanceParcourue;
        this.dateDepart = dateDepart;
        this.dateRetour = dateRetour;
        this.assignations = new ArrayList<>();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdVehicule() {
        return idVehicule;
    }
    
    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }
    
    public Double getDistanceParcourue() {
        return distanceParcourue;
    }
    
    public void setDistanceParcourue(Double distanceParcourue) {
        this.distanceParcourue = distanceParcourue;
    }
    
    public LocalDateTime getDateDepart() {
        return dateDepart;
    }
    
    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }
    
    public LocalDateTime getDateRetour() {
        return dateRetour;
    }
    
    public void setDateRetour(LocalDateTime dateRetour) {
        this.dateRetour = dateRetour;
    }
    
    public Vehicule getVehicule() {
        return vehicule;
    }
    
    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
        if (vehicule != null) {
            this.idVehicule = vehicule.getId();
        }
    }
    
    public List<Assignation> getAssignations() {
        return assignations;
    }
    
    public void setAssignations(List<Assignation> assignations) {
        this.assignations = assignations;
    }
    
    public void addAssignation(Assignation assignation) {
        this.assignations.add(assignation);
        assignation.setTrajet(this);
    }
    
    public long getDureeEnMinutes() {
        if (dateDepart != null && dateRetour != null) {
            return java.time.Duration.between(dateDepart, dateRetour).toMinutes();
        }
        return 0;
    }
    
    public int getNombreReservations() {
        return assignations != null ? assignations.size() : 0;
    }
    
    @Override
    public String toString() {
        return "Trajet{" +
                "id=" + id +
                ", idVehicule=" + idVehicule +
                ", distanceParcourue=" + distanceParcourue +
                ", dateDepart=" + dateDepart +
                ", dateRetour=" + dateRetour +
                ", nbReservations=" + getNombreReservations() +
                '}';
    }
}