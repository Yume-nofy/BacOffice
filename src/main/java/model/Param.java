package model;

public class Param {
    private int id; 
    private double vitesse_moyenne;
    private int temps_attente;

    public Param() {
    }

    public Param(int id, double vitesse_moyenne, int temps_attente) {
        this.id = id;
        this.vitesse_moyenne = vitesse_moyenne;
        this.temps_attente = temps_attente;
    }

    public int getId() {
        return id;
    }

    public int getTemps_attente() {
        return temps_attente;
    }

    public double getVitesse_moyenne() {
        return vitesse_moyenne;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTemps_attente(int temps_attente) {
        this.temps_attente = temps_attente;
    }

    public void setVitesse_moyenne(double vitesse_moyenne) {
        this.vitesse_moyenne = vitesse_moyenne;
    }
}
