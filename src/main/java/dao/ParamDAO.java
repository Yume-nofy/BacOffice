package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Param;
import util.DBConnection;

public class ParamDAO {
    public Param getParam() {
        Param p = new Param();
        String sql = "SELECT * FROM parametre limit 1";
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                p.setId(rs.getInt("id"));
                p.setVitesse_moyenne(rs.getDouble("vitesse_moyenne"));
                p.setTemps_attente(rs.getInt("temps_attente"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return p;
    }
}
