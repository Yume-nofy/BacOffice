package model;

import java.math.BigDecimal;
import dao.DistanceDAO;

public class Lieu {
    private int id;
    private String libelle;
    private String code;
    
    public Lieu() {}
    
    public Lieu(String libelle, String code) {
        this.libelle = libelle;
        this.code = code;
    }
    
    public Lieu(int id, String libelle, String code) {
        this.id = id;
        this.libelle = libelle;
        this.code = code;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public Double calculeDistance(Lieu autreLieu) {
        DistanceDAO distanceDAO = new DistanceDAO();
        Distance distance = distanceDAO.getDistanceBetween(this.id, autreLieu.getId());
        
        if (distance != null) {
            BigDecimal kilometer = distance.getKilometer();
            if (kilometer != null) {
                return kilometer.doubleValue();
            }
        }
        return 0.0;
    }
    
    @Override
    public String toString() {
        return "Lieu{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}