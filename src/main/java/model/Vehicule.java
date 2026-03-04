package model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import dao.ParamDAO;
import dao.LieuDAO;

public class Vehicule {
    private int id;
    private String reference;
    private String typeCarburant; // D: Diesel, Es: Essence, El: Electrique
    private int nbrPlace;
    private boolean isOccuped;

    private List<Reservation> reservationsAssign;
    private List<Lieu> lieux;
    private Double distanceTotal;
    private LocalDateTime dateRetour;
    
    public Vehicule() {}
    
    public Vehicule(String reference, String typeCarburant, int nbrPlace) {
        this.reference = reference;
        this.typeCarburant = typeCarburant;
        this.nbrPlace = nbrPlace;
        this.reservationsAssign= new ArrayList<>();
        this.lieux = new ArrayList<>();
        this.distanceTotal = 0.0;
        this.dateRetour = null;
    }
    
    public Vehicule(int id, String reference, String typeCarburant, int nbrPlace) {
        this.id = id;
        this.reference = reference;
        this.typeCarburant = typeCarburant;
        this.nbrPlace = nbrPlace;
        this.reservationsAssign= new ArrayList<>();
        this.lieux = new ArrayList<>();
        this.distanceTotal = 0.0;
        this.dateRetour = null;
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
    
    public List<Lieu> getLieux() {
        return lieux;
    }
    
    public void setLieux(List<Lieu> lieux) {
        this.lieux = lieux;
    }
    
    public Double getDistanceTotal() {
        return distanceTotal;
    }
    
    public void setDistanceTotal(Double distanceTotal) {
        this.distanceTotal = distanceTotal;
    }
    
    public LocalDateTime getDateRetour() {
        return dateRetour;
    }
    
    public void setDateRetour(LocalDateTime dateRetour) {
        this.dateRetour = dateRetour;
    }
    
    public LocalDateTime getdateretourAssign() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
        LocalDateTime defaultDate = LocalDateTime.parse("11/11/11 11:11:11", formatter);
        
        // Vérifier que toutes les réservations sont assignées
        if (reservationsAssign == null || reservationsAssign.isEmpty()) {
            return defaultDate;
        }
        
        // Récupérer les paramètres (vitesse moyenne)
        ParamDAO paramDAO = new ParamDAO();
        Param param = paramDAO.getParam();
        double vitesseMoyenne = param.getVitesse_moyenne(); // km/h
        
        // L'aéroport a toujours l'ID 1
        int idAeroport = 1;
        
        // Récupérer les lieux
        LieuDAO lieuDAO = new LieuDAO();
        Lieu aeroportLieu = lieuDAO.getLieuById(idAeroport);
        
        if (aeroportLieu == null) {
            return defaultDate;
        }
        
        // Initialiser les listes
        List<Lieu> lieusList = new ArrayList<>();
        Double distanceTotalValue = 0.0;
        
        // Créer une liste temporaire de lieux UNIQUES à partir des réservations
        // Éviter les doublons : même lieu et même date
        List<Lieu> lieuDisponibles = new ArrayList<>();
        Set<String> lieuDateSet = new java.util.HashSet<>();
        
        for (Reservation reservation : reservationsAssign) {
            Lieu hotelLieu = lieuDAO.getLieuById(reservation.getIdHotel());
            if (hotelLieu != null) {
                // Créer une clé unique pour chaque lieu + date
                String cleLieuDate = hotelLieu.getId() + "_" + reservation.getDateArrivee().toLocalDate();
                
                // Ajouter seulement si c'est la première fois qu'on voit ce lieu avec cette date
                if (!lieuDateSet.contains(cleLieuDate)) {
                    lieuDisponibles.add(hotelLieu);
                    lieuDateSet.add(cleLieuDate);
                }
            }
        }
        
        if (lieuDisponibles.isEmpty()) {
            return defaultDate;
        }
        
        // Parcourir les lieux : partir de l'aéroport et aller au lieu le plus proche
        Lieu lieuActuel = aeroportLieu;
        Lieu dernierLieu = null;
        
        while (!lieuDisponibles.isEmpty()) {
            Lieu lieuPlusProche = null;
            Double distanceMinimale = Double.MAX_VALUE;
            
            // Trouver le lieu le plus proche du lieu actuel
            for (Lieu lieu : lieuDisponibles) {
                if(lieu.getId() == lieuActuel.getId()) {
                    continue; // Ignorer le lieu actuel
                }
                Double distance = lieuActuel.calculeDistance(lieu);
                
                if (distance != null && distance > 0) {
                    // Si la distance est plus petite
                    if (distance < distanceMinimale) {
                        distanceMinimale = distance;
                        lieuPlusProche = lieu;
                    }
                    // Si la distance est égale, comparer alphabétiquement les noms des lieux
                    else if (distance.equals(distanceMinimale) && lieuPlusProche != null) {
                        if (lieu.getLibelle().compareTo(lieuPlusProche.getLibelle()) < 0) {
                            lieuPlusProche = lieu;
                        }
                    }
                }
            }
            
            if (lieuPlusProche == null || distanceMinimale == Double.MAX_VALUE) {
                break;
            }
            
            // Ajouter le lieu à la liste
            lieusList.add(lieuPlusProche);
            dernierLieu = lieuPlusProche;
            
            // Enlever le lieu de la liste des disponibles
            lieuDisponibles.remove(lieuPlusProche);
            
            // Le lieu actuel devient le lieu le plus proche pour la prochaine itération
            lieuActuel = lieuPlusProche;
        }
        
        if (dernierLieu == null) {
            return defaultDate;
        }
        
        reservationsAssign.sort((r1, r2) -> r1.getDateArrivee().compareTo(r2.getDateArrivee()));
        LocalDateTime dateDepart = reservationsAssign.get(0).getDateArrivee();
        
        // Récupérer la distance DIRECTE entre l'aéroport et le dernier lieu (pas la somme)
        Double distanceDirect = aeroportLieu.calculeDistance(dernierLieu);
        distanceTotalValue = (distanceDirect != null && distanceDirect > 0) ? distanceDirect : 0.0;
        
        // Calculer le temps de trajet en minutes
        double tempsTrajet = (distanceTotalValue / vitesseMoyenne) * 60; // conversion en minutes
        
        // Ajouter le temps total à la date de départ SANS ajouter le temps d'attente
        long totalTemps = (long) tempsTrajet; // en minutes
        
        // Calculer la date de retour
        LocalDateTime dateRetourValue = dateDepart.plusMinutes(totalTemps);
        
        // Setter les attributs via les setters
        setLieux(lieusList);
        setDistanceTotal(distanceTotalValue);
        setDateRetour(dateRetourValue);
        
        return dateRetourValue;
    }
}