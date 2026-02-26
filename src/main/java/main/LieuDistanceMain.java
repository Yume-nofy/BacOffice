package main;

import dao.LieuDAO;
import dao.DistanceDAO;
import model.Lieu;
import model.Distance;

import java.math.BigDecimal;
import java.util.List;

public class LieuDistanceMain {
    
    private static final LieuDAO lieuDAO = new LieuDAO();
    private static final DistanceDAO distanceDAO = new DistanceDAO();
    
    private static void insertLieux() {
        System.out.println("\n INSERTION DES LIEUX");
        System.out.println("──────────────────────────");
        
        Lieu[] lieux = {
            new Lieu("Colbert", "COL"),
            new Lieu("Novotel", "NOV"),
            new Lieu("Ibis", "IBI"),
            new Lieu("Lokanga", "LOK")
        };
        
        for (Lieu lieu : lieux) {
            Lieu existing = lieuDAO.getLieuByCode(lieu.getCode());
            if (existing == null) {
                lieuDAO.addLieu(lieu);
                System.out.printf(" Ajouté : %-10s (Code: %s, ID: %d)%n", 
                    lieu.getLibelle(), lieu.getCode(), lieu.getId());
            } else {
                lieu.setId(existing.getId());
                System.out.printf("⏭  Déjà existant : %-10s (Code: %s, ID: %d)%n", 
                    lieu.getLibelle(), lieu.getCode(), lieu.getId());
            }
        }
    }
    
    private static void insertDistances() {
        System.out.println("\n INSERTION DES DISTANCES");
        System.out.println("──────────────────────────");
        
        Lieu colbert = lieuDAO.getLieuByCode("COL");
        Lieu novotel = lieuDAO.getLieuByCode("NOV");
        Lieu ibis = lieuDAO.getLieuByCode("IBI");
        Lieu lokanga = lieuDAO.getLieuByCode("LOK");
        
        if (colbert == null || novotel == null || ibis == null || lokanga == null) {
            System.out.println(" Certains lieux n'existent pas. Veuillez d'abord insérer les lieux.");
            return;
        }
        
        Object[][] distancesData = {
            {colbert, novotel, 2.5},
            {colbert, ibis, 1.8},
            {colbert, lokanga, 0.5},
            {novotel, ibis, 3.2},
            {novotel, lokanga, 2.8},
            {ibis, lokanga, 2.1}
        };
        
        int count = 0;
        for (Object[] data : distancesData) {
            Lieu from = (Lieu) data[0];
            Lieu to = (Lieu) data[1];
            double km = (Double) data[2];
            
            Distance distance = new Distance(from, to, BigDecimal.valueOf(km));
            distanceDAO.addDistance(distance);
            
            System.out.printf(" Ajouté : %-10s -> %-10s : %.1f km%n", 
                from.getLibelle(), to.getLibelle(), km);
            count++;
        }
        
        System.out.printf("%nTotal : %d distances insérées%n", count);
    }
    
    private static void displayLieux() {
        System.out.println("\n LISTE DES LIEUX");
        System.out.println("──────────────────────────");
        List<Lieu> lieux = lieuDAO.getAllLieux();
        
        if (lieux.isEmpty()) {
            System.out.println("Aucun lieu trouvé");
            return;
        }
        
        System.out.printf("%-5s %-15s %-5s%n", "ID", "Libellé", "Code");
        System.out.println("──────────────────────────");
        for (Lieu l : lieux) {
            System.out.printf("%-5d %-15s %-5s%n", l.getId(), l.getLibelle(), l.getCode());
        }
    }
    
    private static void displayDistances() {
        System.out.println("\n📏 LISTE DES DISTANCES");
        System.out.println("────────────────────────────────────────────");
        List<Distance> distances = distanceDAO.getAllDistances();
        
        if (distances.isEmpty()) {
            System.out.println("Aucune distance trouvée");
            return;
        }
        
        System.out.printf("%-5s %-15s -> %-15s %-10s%n", "ID", "De", "Vers", "Kilomètres");
        System.out.println("────────────────────────────────────────────");
        for (Distance d : distances) {
            System.out.printf("%-5d %-15s -> %-15s %.2f km%n", 
                d.getId(), 
                d.getFromLieu().getLibelle(), 
                d.getToLieu().getLibelle(), 
                d.getKilometer());
        }
    }
    
    private static void displayDistancesFromLieu(String code) {
        Lieu lieu = lieuDAO.getLieuByCode(code);
        if (lieu == null) {
            System.out.println(" Lieu non trouvé avec le code: " + code);
            return;
        }
        
        System.out.printf("%n📏 DISTANCES DEPUIS %s%n", lieu.getLibelle().toUpperCase());
        System.out.println("────────────────────────────");
        List<Distance> distances = distanceDAO.getDistancesFromLieu(lieu.getId());
        
        if (distances.isEmpty()) {
            System.out.println("Aucune distance enregistrée depuis ce lieu");
            return;
        }
        
        for (Distance d : distances) {
            System.out.printf("  → %-10s : %.2f km%n", 
                d.getToLieu().getLibelle(), 
                d.getKilometer());
        }
    }
    
    public static void main(String[] args) {
        System.out.println("\n DÉBUT DU SEEDER LIEU ET DISTANCE");
        System.out.println("═══════════════════════════════════════");
        
        try {
            if (args.length > 0 && args[0].equals("--display")) {
                displayLieux();
                displayDistances();
                
                if (args.length > 1) {
                    displayDistancesFromLieu(args[1]);
                }
                return;
            }
            
            insertLieux();
            
            insertDistances();
            
            System.out.println("\n RÉSUMÉ FINAL");
            System.out.println("═══════════════════════════════════════");
            displayLieux();
            displayDistances();
            
            displayDistancesFromLieu("COL");
            
        } catch (Exception e) {
            System.err.println("\n Erreur: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n FIN DU SEEDER");
    }
}