/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjetosDAO;

import Objetos.ConnectionFactory;
import Objetos.Day_tip;
import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kaiof
 */
public class Day_tipDAO extends Objetos.Day_tip {

    public Day_tipDAO(int idDay_tip, String day_tip_text, String day_tip_title, String url_image) {
        super(idDay_tip, day_tip_text, day_tip_title, url_image);
    }

    public Day_tipDAO() {
    }
    
    public static Day_tip get_day_tip(int day_tip_id){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Day_tip day_t = null;
        try {
            stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM day_tip WHERE idDay_tip  =?");
            stmt.setInt(1,day_tip_id);
            rs = stmt.executeQuery();
            
            if(rs.next()){
                day_t = new Day_tip();
                day_t.setDay_tip_title(rs.getString("day_tip_title"));
                day_t.setDay_tip_text(rs.getString("day_tip_text"));
                day_t.setUrl_image(rs.getString("url_image"));;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Day_tipDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    
        return day_t;
    }
}
