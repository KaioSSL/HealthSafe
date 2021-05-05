/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjetosDAO;

import Objetos.ConnectionFactory;
import Objetos.Day_atualization;
import Objetos.User;
import com.mysql.jdbc.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author kaiof
 */
public class Day_atualizationDAO extends Objetos.Day_atualization {

    public Day_atualizationDAO(Date day_atualization, float systolic_pressure, float distolic_pressure, float weight, boolean alcool, boolean smoke, boolean exercise, boolean fast_food, String classification_pressure, int day_tip, String userLogin, String classification_imc, float IMC, int idDay_Atualization) {
        super(day_atualization, systolic_pressure, distolic_pressure, weight, alcool, smoke, exercise, fast_food, classification_pressure, day_tip, userLogin, classification_imc, IMC, idDay_Atualization);
    }


    
    public Day_atualizationDAO() {
    }
    
    public void insert_day_atualization(Day_atualization day_atualization){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM Day_Atualization WHERE User_login = ? AND day_Atualization = curdate()");
            stmt.setString(1,day_atualization.getUserLogin());
            rs = stmt.executeQuery();
            
            if(!rs.next()){

                stmt = (PreparedStatement) con.prepareStatement("INSERT INTO Day_Atualization(systolic_pressure,diastolic_pressure,weight,alcool,smoke,exercise,fast_food,User_login,"
                        + "classification_pressure,classification_imc,imc,day_tip,day_atualization) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,curdate())");
                stmt.setFloat(1,day_atualization.getSystolic_pressure());
                stmt.setFloat(2,day_atualization.getDistolic_pressure());
                stmt.setFloat(3,day_atualization.getWeight());
                stmt.setBoolean(4,day_atualization.isAlcool());
                stmt.setBoolean(5,day_atualization.isSmoke());
                stmt.setBoolean(6,day_atualization.isExercise());
                stmt.setBoolean(7,day_atualization.isFast_food());
                stmt.setString(8,day_atualization.getUserLogin());
                stmt.setString(9, day_atualization.getClassification_pressure());
                stmt.setString(10,day_atualization.getClassification_imc());
                stmt.setFloat(11,day_atualization.getIMC());
                stmt.setInt(12,day_atualization.getDay_tip());

                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null,"Atualização Diária registrada com sucesso");
            }else{
                Day_atualizationDAO.update_day_atualization(day_atualization);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }finally{
            ConnectionFactory.closeConnection(con,stmt);
        }
        
    
    }
    
    public static void update_day_atualization(Day_atualization day_atualization){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("UPDATE Day_Atualization SET systolic_pressure = ?, diastolic_pressure = ?, weight = ?,"
                    + "alcool = ?, smoke = ?,exercise = ?,fast_food = ?, classification_pressure = ?,day_tip = ?, classification_imc = ?, IMC = ? WHERE User_login = ? AND day_atualization = curdate()");
            stmt.setFloat(1,day_atualization.getSystolic_pressure());
            stmt.setFloat(2,day_atualization.getDistolic_pressure());
            stmt.setFloat(3,day_atualization.getWeight());
            stmt.setBoolean(4, day_atualization.isAlcool());
            stmt.setBoolean(5,day_atualization.isSmoke());
            stmt.setBoolean(6, day_atualization.isExercise());
            stmt.setBoolean(7,day_atualization.isFast_food());
            stmt.setString(8, day_atualization.getClassification_pressure());
            stmt.setInt(9, day_atualization.getDay_tip());
            stmt.setString(10,day_atualization.getClassification_imc());
            stmt.setFloat(11,day_atualization.getIMC());
            stmt.setString(12, day_atualization.getUserLogin());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Avaliação diária alterada com sucesso");
        } catch (SQLException ex) {
            Logger.getLogger(Day_atualizationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con,stmt);
        }
    
    }
    
    public static void delete_day_atualization_user(User user){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("DELETE FROM Day_atualization WHERE User_login = ?");
            stmt.setString(1,user.getLogin());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,ex);
        }finally{
            ConnectionFactory.closeConnection(con,stmt);
        }

    }
    
    public static int day_atualization_count(String login){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("SELECT count(idDay_atualization) as count FROM Day_atualization WHERE User_login = ?");
            stmt.setString(1, login);
            rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt("count");
            }else{
                return 0;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro na função de conta de atualizações diarias "+ ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        
        
        return 0;
    }
    public static Day_atualization get_recent_day_atualization(String login){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Day_atualization day_a = new Day_atualization();
        try {
            stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM Day_Atualization WHERE User_login = ? ORDER BY idDay_atualization desc LIMIT 1");
            stmt.setString(1,login);
            rs = stmt.executeQuery();
            
            if(rs.next()){
                day_a.setIdDay_Atualization(rs.getInt("idDay_atualization"));
                day_a.setDay_Atualization(rs.getDate("day_atualization"));
                day_a.setSystolic_pressure(rs.getFloat("systolic_pressure"));
                day_a.setDiastolic_pressure(rs.getFloat("diastolic_pressure"));
                day_a.setWeight(rs.getFloat("weight"));
                day_a.setAlcool(rs.getBoolean("alcool"));
                day_a.setSmoke(rs.getBoolean("smoke"));
                day_a.setExercise(rs.getBoolean("exercise"));
                day_a.setFast_food(rs.getBoolean("fast_food"));
                day_a.setClassification_pressure(rs.getString("classification_pressure"));
                day_a.setClassification_imc(rs.getString("classification_imc"));
                day_a.setDay_tip(rs.getInt("day_tip"));
                day_a.setIMC(rs.getFloat("IMC"));
                return day_a;
            }else{
                //
            }
        } catch (SQLException ex) {
            Logger.getLogger(Day_atualizationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return day_a;
    }
    public static ArrayList<Day_atualization> get_day_atualization(String login){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Day_atualization> day_a_list = new ArrayList<Day_atualization>();
        try {
            stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM Day_Atualization WHERE User_login = ? ORDER BY day_atualization ");
            stmt.setString(1,login);
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Day_atualization day_a = new Day_atualization();
                day_a.setIdDay_Atualization(rs.getInt("idDay_atualization"));
                day_a.setDay_Atualization(rs.getDate("day_atualization"));
                day_a.setSystolic_pressure(rs.getFloat("systolic_pressure"));
                day_a.setDiastolic_pressure(rs.getFloat("diastolic_pressure"));
                day_a.setWeight(rs.getFloat("weight"));
                day_a.setAlcool(rs.getBoolean("alcool"));
                day_a.setSmoke(rs.getBoolean("smoke"));
                day_a.setExercise(rs.getBoolean("exercise"));
                day_a.setFast_food(rs.getBoolean("fast_food"));
                day_a.setClassification_pressure(rs.getString("classification_pressure"));
                day_a.setClassification_imc(rs.getString("classification_imc"));
                day_a.setDay_tip(rs.getInt("day_tip"));
                day_a.setIMC(rs.getFloat("IMC"));
                day_a_list.add(day_a);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Day_atualizationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con,stmt,rs);
        }
        return day_a_list;
    }
    public static ArrayList<Day_atualization> get_day_atualization(String login,Date data_inicio,Date data_fim){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Day_atualization> day_a_list = new ArrayList<>();
        try {
            
            if(data_inicio == null && data_fim != null){
                stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM Day_Atualization WHERE User_login = ? AND day_atualization <= ? ORDER BY day_atualization");
                stmt.setString(1,login);
                stmt.setDate(2, (java.sql.Date) data_fim);
                rs = stmt.executeQuery();
            }else if(data_inicio !=null && data_fim == null){
                stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM Day_Atualization WHERE User_login = ? AND day_atualization >= ? ORDER BY day_atualization");
                stmt.setString(1,login);
                stmt.setDate(2, (java.sql.Date) data_inicio);
                rs = stmt.executeQuery();
            }else if(data_fim != null && data_inicio != null){
                stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM Day_Atualization WHERE User_login = ? AND day_atualization >= ? AND day_atualization <= ? ORDER BY day_atualization");
                stmt.setString(1,login);
                stmt.setDate(2, (java.sql.Date) data_inicio);
                stmt.setDate(3, (java.sql.Date) data_fim);
                rs = stmt.executeQuery();
            
            }else{
                return get_day_atualization(login);
            }
            
            while(rs.next()){
                Day_atualization day_a = new Day_atualization();
                day_a.setIdDay_Atualization(rs.getInt("idDay_atualization"));
                day_a.setDay_Atualization(rs.getDate("day_atualization"));
                day_a.setSystolic_pressure(rs.getFloat("systolic_pressure"));
                day_a.setDiastolic_pressure(rs.getFloat("diastolic_pressure"));
                day_a.setWeight(rs.getFloat("weight"));
                day_a.setAlcool(rs.getBoolean("alcool"));
                day_a.setSmoke(rs.getBoolean("smoke"));
                day_a.setExercise(rs.getBoolean("exercise"));
                day_a.setFast_food(rs.getBoolean("fast_food"));
                day_a.setClassification_pressure(rs.getString("classification_pressure"));
                day_a.setClassification_imc(rs.getString("classification_imc"));
                day_a.setDay_tip(rs.getInt("day_tip"));
                day_a.setIMC(rs.getFloat("IMC"));
                day_a_list.add(day_a);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Day_atualizationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con,stmt,rs);
        }
        return day_a_list;
    }
    
    public static Day_atualization get_day_atualization(String login,Date data){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Day_atualization day_a = null;
        try {

                stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM Day_Atualization WHERE User_login = ? AND day_atualization = ? ORDER BY day_atualization");
                stmt.setString(1,login);
                stmt.setDate(2, (java.sql.Date) data);
                rs = stmt.executeQuery();

            
            if(rs.next()){
                day_a = new Day_atualization();
                day_a.setIdDay_Atualization(rs.getInt("idDay_atualization"));
                day_a.setDay_Atualization(rs.getDate("day_atualization"));
                day_a.setSystolic_pressure(rs.getFloat("systolic_pressure"));
                day_a.setDiastolic_pressure(rs.getFloat("diastolic_pressure"));
                day_a.setWeight(rs.getFloat("weight"));
                day_a.setAlcool(rs.getBoolean("alcool"));
                day_a.setSmoke(rs.getBoolean("smoke"));
                day_a.setExercise(rs.getBoolean("exercise"));
                day_a.setFast_food(rs.getBoolean("fast_food"));
                day_a.setClassification_pressure(rs.getString("classification_pressure"));
                day_a.setClassification_imc(rs.getString("classification_imc"));
                day_a.setDay_tip(rs.getInt("day_tip"));
                day_a.setIMC(rs.getFloat("IMC"));
                return day_a;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Day_atualizationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con,stmt,rs);
        }
        return day_a;
    }
}
