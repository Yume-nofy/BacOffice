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
    private List<LocalDateTime> retourListDate;
    private LocalDateTime dateRetour;
    
    public Vehicule() {}
    
    public Vehicule(String reference, String typeCarburant, int nbrPlace) {
        this.reference = reference;
        this.typeCarburant = typeCarburant;
        this.nbrPlace = nbrPlace;
        this.reservationsAssign= new ArrayList<>();
        this.lieux = new ArrayList<>();
        this.distanceTotal = 0.0;
        this.retourListDate = new ArrayList<>();
    }
    
    public Vehicule(int id, String reference, String typeCarburant, int nbrPlace) {
        this.id = id;
        this.reference = reference;
        this.typeCarburant = typeCarburant;
        this.nbrPlace = nbrPlace;
        this.reservationsAssign= new ArrayList<>();
        this.lieux = new ArrayList<>();
        this.distanceTotal = 0.0;
        this.retourListDate = new ArrayList<>();
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

    public List<LocalDateTime> getRetourListDate() {
        return retourListDate;
    }

    public void setDateRetour(LocalDateTime dateRetour) {
        this.dateRetour = dateRetour;
    }

    public void setRetourListDate(List<LocalDateTime> retourListDate) {
        this.retourListDate = retourListDate;
    }

    public LocalDateTime getdateretourAssign() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
        LocalDateTime defaultDate = LocalDateTime.parse("11/11/11 11:11:11", formatter);
        
        if (reservationsAssign == null || reservationsAssign.isEmpty()) {
            return defaultDate;
        }
        
        ParamDAO paramDAO = new ParamDAO();
        Param param = paramDAO.getParam();
        double vitesseMoyenne = param.getVitesse_moyenne();
        
        int idAeroport = 1;
        
        LieuDAO lieuDAO = new LieuDAO();
        Lieu aeroportLieu = lieuDAO.getLieuById(idAeroport);
        
        if (aeroportLieu == null) {
            return defaultDate;
        }
        
        List<Lieu> lieusList = new ArrayList<>();
        Double distanceTotalValue = 0.0;
        
        List<Lieu> lieuDisponibles = new ArrayList<>();
        Set<String> lieuDateSet = new java.util.HashSet<>();
        
        for (Reservation reservation : this.reservationsAssign) {
            Lieu hotelLieu = lieuDAO.getLieuById(reservation.getIdHotel());
            if (hotelLieu != null) {
                String cleLieuDate = hotelLieu.getId() + "_" + reservation.getDateArrivee().toLocalDate();
                
                if (!lieuDateSet.contains(cleLieuDate)) {
                    lieuDisponibles.add(hotelLieu);
                    lieuDateSet.add(cleLieuDate);
                }
            }
        }
        
        if (lieuDisponibles.isEmpty()) {
            return defaultDate;
        }
        
        Lieu lieuActuel = aeroportLieu;
        Lieu dernierLieu = null;
        Double distanceDirect =0.0;
        List<LocalDateTime> datRet = new ArrayList<>();
        LocalDateTime dateActuel=  this.reservationsAssign.get(0).getDateArrivee();
        while (!lieuDisponibles.isEmpty()) {
            Lieu lieuPlusProche = null;
            Double distanceMinimale = Double.MAX_VALUE;
            for (Lieu lieu : lieuDisponibles) {
                if(lieu.getId() == lieuActuel.getId()) {
                    continue; 
                }
                
                Double distance = lieuActuel.calculeDistance(lieu);
                if (distance != null && distance > 0) {
                    if (distance < distanceMinimale) {
                        distanceMinimale = distance;
                        lieuPlusProche = lieu;
                    } else if (distance.equals(distanceMinimale) && lieuPlusProche != null) {
                        if (lieu.getLibelle().compareTo(lieuPlusProche.getLibelle()) < 0) {
                            lieuPlusProche = lieu;
                        }
                    }
                }
            }
            
            if (lieuPlusProche == null || distanceMinimale == Double.MAX_VALUE) {
                break;
            }
            double tmpT = (distanceMinimale/vitesseMoyenne)*60;
            System.out.println("date actuel : "+ dateActuel);
            System.out.println("minute trajet : "+tmpT);
            dateActuel= dateActuel.plusMinutes((long)tmpT);
            datRet.add(dateActuel);
            lieusList.add(lieuPlusProche);
            distanceDirect += lieuActuel.calculeDistance(lieuPlusProche);
            dernierLieu = lieuPlusProche;
            
            lieuDisponibles.remove(lieuPlusProche);
            
            lieuActuel = lieuPlusProche;
        }
        
        if (dernierLieu == null) {
            return defaultDate;
        }
        
        reservationsAssign.sort((r1, r2) -> r1.getDateArrivee().compareTo(r2.getDateArrivee()));
        LocalDateTime dateDepart = reservationsAssign.get(0).getDateArrivee();
        
        distanceDirect += aeroportLieu.calculeDistance(dernierLieu);
        distanceTotalValue = (distanceDirect != null && distanceDirect > 0) ? distanceDirect : 0.0;
        System.out.println("distanceTotalValue :"+distanceTotalValue);
        double tempsTrajet = (distanceTotalValue / vitesseMoyenne) * 60; 
        
        long totalTemps = (long) tempsTrajet; 
        
        LocalDateTime dateRetourValue = dateDepart.plusMinutes(totalTemps);
        
        setLieux(lieusList);
        setDistanceTotal(distanceTotalValue);
        setRetourListDate(datRet);
        setDateRetour(dateRetourValue);
        
        return dateRetourValue;
    }
}