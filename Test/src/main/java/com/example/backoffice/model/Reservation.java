package com.example.backoffice.model;

import java.time.LocalDateTime;

public class Reservation {

    private Integer id;
    private String idClient;
    private Integer nombrePassager;
    private LocalDateTime dateArrivee;
    private Hotel hotel;
    private boolean nonassigne= false; 

    public Reservation() {
    }

    public Reservation(Integer id, String idClient, Integer nombrePassager, LocalDateTime dateArrivee, Hotel hotel) {
        this.id = id;
        this.idClient = idClient;
        this.nombrePassager = nombrePassager;
        this.dateArrivee = dateArrivee;
        this.hotel = hotel;
        this.nonassigne = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public Integer getNombrePassager() {
        return nombrePassager;
    }

    public void setNombrePassager(Integer nombrePassager) {
        this.nombrePassager = nombrePassager;
    }

    public LocalDateTime getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public boolean getNonAssigne() {
        return this.nonassigne;
    }

    public void setNonassigne(boolean nonassigne) {
        this.nonassigne = nonassigne;
    }
}
