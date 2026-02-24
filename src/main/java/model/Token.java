package model;

import java.time.LocalDateTime;

public class Token {
    private int id;
    private String token;
    private LocalDateTime dateExpiration;
    private LocalDateTime dateCreation;
    
    public Token() {}
    
    public Token(String token, LocalDateTime dateExpiration) {
        this.token = token;
        this.dateExpiration = dateExpiration;
    }
    
    public Token(int id, String token, LocalDateTime dateExpiration, LocalDateTime dateCreation) {
        this.id = id;
        this.token = token;
        this.dateExpiration = dateExpiration;
        this.dateCreation = dateCreation;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }
    
    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
    
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    // Méthodes utilitaires
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(dateExpiration);
    }
    
    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", dateExpiration=" + dateExpiration +
                ", dateCreation=" + dateCreation +
                '}';
    }
}