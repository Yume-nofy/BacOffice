package model;

import java.math.BigDecimal;

public class Distance {
    private int id;
    private Lieu fromLieu;
    private Lieu toLieu;
    private BigDecimal kilometer;
    
    // Constructeurs
    public Distance() {}
    
    public Distance(Lieu fromLieu, Lieu toLieu, BigDecimal kilometer) {
        this.fromLieu = fromLieu;
        this.toLieu = toLieu;
        this.kilometer = kilometer;
    }
    
    public Distance(int id, Lieu fromLieu, Lieu toLieu, BigDecimal kilometer) {
        this.id = id;
        this.fromLieu = fromLieu;
        this.toLieu = toLieu;
        this.kilometer = kilometer;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Lieu getFromLieu() {
        return fromLieu;
    }
    
    public void setFromLieu(Lieu fromLieu) {
        this.fromLieu = fromLieu;
    }
    
    public Lieu getToLieu() {
        return toLieu;
    }
    
    public void setToLieu(Lieu toLieu) {
        this.toLieu = toLieu;
    }
    
    public BigDecimal getKilometer() {
        return kilometer;
    }
    
    public void setKilometer(BigDecimal kilometer) {
        this.kilometer = kilometer;
    }
    
    @Override
    public String toString() {
        return "Distance{" +
                "id=" + id +
                ", from=" + fromLieu.getLibelle() +
                " -> to=" + toLieu.getLibelle() +
                ", km=" + kilometer +
                '}';
    }
}