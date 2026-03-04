package model;

import java.util.List;
import java.util.ArrayList;

public class Vehicule {
    private int id;
    private String reference;
    private String typeCarburant; // D: Diesel, Es: Essence, El: Electrique
    private int nbrPlace;
    private boolean isOccuped;

    private List<Reservation> reservationsAssign;
    
    public Vehicule() {}
    
    public Vehicule(String reference, String typeCarburant, int nbrPlace) {
        this.reference = reference;
        this.typeCarburant = typeCarburant;
        this.nbrPlace = nbrPlace;
        this.reservationsAssign= new ArrayList<>();
    }
    
    public Vehicule(int id, String reference, String typeCarburant, int nbrPlace) {
        this.id = id;
        this.reference = reference;
        this.typeCarburant = typeCarburant;
        this.nbrPlace = nbrPlace;
        this.reservationsAssign= new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getReference() {
        return reference;
    }
    
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    public String getTypeCarburant() {
        return typeCarburant;
    }
    
    public void setTypeCarburant(String typeCarburant) {
        this.typeCarburant = typeCarburant;
    }
    
    public int getNbrPlace() {
        return nbrPlace;
    }
    
    public void setNbrPlace(int nbrPlace) {
        this.nbrPlace = nbrPlace;
    }
    
    public String getTypeCarburantLibelle() {
        switch(typeCarburant) {
            case "D": return "Diesel";
            case "Es": return "Essence";
            case "El": return "electrique";
            default: return typeCarburant;
        }
    }
    
    @Override
    public String toString() {
        return "Vehicule{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", typeCarburant='" + typeCarburant + '\'' +
                ", nbrPlace=" + nbrPlace +
                '}';
    }

    public List<Reservation> getReservationsAssign() {
        return reservationsAssign;
    }

    public void setReservationsAssign(List<Reservation> reservationsAssign) {
        this.reservationsAssign = reservationsAssign;
    }

    public void setIsOccuped(boolean isOccuped) {
        this.isOccuped = isOccuped;
    }

    public boolean IsOccuped() {
        return isOccuped;
    }

    public int getNbrPlaceDisponible() {
        int c = 0;
        for (Reservation reservation : reservationsAssign) {
            c += reservation.getNbPassager();
        }
        return this.nbrPlace-c;
    }
}