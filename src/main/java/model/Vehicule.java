package model;

public class Vehicule {
    private int id;
    private String reference;
    private String typeCarburant; // D: Diesel, Es: Essence, El: Electrique
    private int nbrPlace;
    
    public Vehicule() {}
    
    public Vehicule(String reference, String typeCarburant, int nbrPlace) {
        this.reference = reference;
        this.typeCarburant = typeCarburant;
        this.nbrPlace = nbrPlace;
    }
    
    public Vehicule(int id, String reference, String typeCarburant, int nbrPlace) {
        this.id = id;
        this.reference = reference;
        this.typeCarburant = typeCarburant;
        this.nbrPlace = nbrPlace;
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
}