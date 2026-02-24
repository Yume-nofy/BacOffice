package main;

import dao.TokenDAO;
import model.Token;
import util.DBConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Random;

public class TokenMain {
    
    private static final Random random = new Random();
    
    private static String generateToken() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder token = new StringBuilder(14);
        for (int i = 0; i < 14; i++) {
            token.append(chars.charAt(random.nextInt(chars.length())));
        }
        return token.toString();
    }
    
    private static void executeSqlScript(String filePath) {
        System.out.println("Exécution du script SQL: " + filePath);
        try (Connection conn = DBConnection.getConnection();
             BufferedReader reader = new BufferedReader(new FileReader(filePath));
             Statement stmt = conn.createStatement()) {
            
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().startsWith("--")) {
                    continue;
                }
                sql.append(line);
                if (line.trim().endsWith(";")) {
                    stmt.execute(sql.toString());
                    sql = new StringBuilder();
                }
            }
            System.out.println("Script exécuté avec succès!");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution du script: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void executeInsertQueries() {
        System.out.println("Exécution des requêtes INSERT en dur...");
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("INSERT INTO token (token, date_expiration) VALUES (" +
                    "'TOKEN_EXPIRE_001', '" + LocalDateTime.now().minusDays(5) + "')");
            
            stmt.executeUpdate("INSERT INTO token (token, date_expiration) VALUES (" +
                    "'TOKEN_EXPIRE_002', '" + LocalDateTime.now().minusHours(2) + "')");
            
            stmt.executeUpdate("INSERT INTO token (token, date_expiration) VALUES (" +
                    "'TOKEN_VALID_001', '" + LocalDateTime.now().plusDays(30) + "')");
            
            stmt.executeUpdate("INSERT INTO token (token, date_expiration) VALUES (" +
                    "'TOKEN_VALID_002', '" + LocalDateTime.now().plusHours(48) + "')");
            
            System.out.println("Requêtes INSERT exécutées avec succès!");
            
        } catch (Exception e) {
            System.err.println("Erreur lors des INSERT: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void insertJavaTokens() {
        System.out.println("Insertion de tokens générés en Java...");
        TokenDAO tokenDAO = new TokenDAO();
        
        Token token1 = new Token(generateToken(), LocalDateTime.now().plusHours(1));
        tokenDAO.addToken(token1);
        System.out.println("Token 1: " + token1.getToken() + " (expire dans 1h)");
        
        Token token2 = new Token(generateToken(), LocalDateTime.now().plusDays(1));
        tokenDAO.addToken(token2);
        System.out.println("Token 2: " + token2.getToken() + " (expire dans 24h)");
        
        Token token3 = new Token(generateToken(), LocalDateTime.now().plusMonths(1));
        tokenDAO.addToken(token3);
        System.out.println("Token 3: " + token3.getToken() + " (expire dans 1 mois)");
        
        Token token4 = new Token(generateToken(), LocalDateTime.now().minusDays(1));
        tokenDAO.addToken(token4);
        System.out.println("Token 4: " + token4.getToken() + " (déjà expiré)");
        
        System.out.println("Total: 4 tokens insérés avec succès!");
    }
    
    public static void main(String[] args) {
        System.out.println("=== DÉBUT DU SEEDER TOKEN ===\n");
        // OPTION 1: Exécuter un script SQL
        // executeSqlScript("sql/script-insert-2026-02-06.sql");
        
        // OPTION 2: Exécuter des requêtes INSERT en dur
        // executeInsertQueries();
        
        // OPTION 3: Insérer des tokens générés en Java (ACTIVÉ PAR DÉFAUT)
        insertJavaTokens();
        
        System.out.println("\n=== TOKENS APRÈS INSERTION ===");
        TokenDAO tokenDAO = new TokenDAO();
        for (Token token : tokenDAO.getAllTokens()) {
            System.out.println("ID: " + token.getId() + 
                             " | Token: " + token.getToken() + 
                             " | Expire: " + token.getDateExpiration() +
                             " | " + (token.isExpired() ? " EXPIRÉ" : " VALIDE"));
        }
        
        System.out.println("\n=== FIN DU SEEDER TOKEN ===");
    }
}