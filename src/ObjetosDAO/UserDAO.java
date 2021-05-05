/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjetosDAO;

import Objetos.ConnectionFactory;
import Objetos.User;
import com.mysql.jdbc.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author kaiof
 */
public class UserDAO extends Objetos.User {

    public UserDAO() {
    }

    public UserDAO(String login, String pass, String name, int smoke_frequency_per_week, int alcool_frequency_per_week, int exercise_frequency_per_week, float height, String sexo, String email, String url_foto, Date data_nascimento, Date data_cadastro) {
        super(login, pass, name, smoke_frequency_per_week, alcool_frequency_per_week, exercise_frequency_per_week, height, sexo, email, url_foto, data_nascimento, data_cadastro);
    }
 
    
    public void insert_user(User user){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try{
            stmt = (PreparedStatement) con.prepareStatement("INSERT INTO User VALUES(?,?,?,?,?,?,?,?,?,?,?,curdate())");
            stmt.setString(1, user.getLogin());
            stmt.setString(2,user.getPass());
            stmt.setString(3,user.getName());
            stmt.setInt(4,user.getSmoke_frequency_per_week());
            stmt.setInt(5,user.getAlcool_frequency_per_week());
            stmt.setInt(6,user.getExercise_frequency_per_week());
            stmt.setFloat(7,user.getHeight());
            stmt.setString(8,user.getSexo());
            stmt.setString(9, user.getEmail());
            stmt.setString(10,user.getUrl_foto());
            stmt.setDate(11, new java.sql.Date(user.getData_nascimento().getTime()));
            
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null,"Usuário cadastrado com sucesso");
        }
        catch(SQLException e){
            if(e.getErrorCode()==1062){
                JOptionPane.showMessageDialog(null,"Esse login já está em uso");
            }else{
                JOptionPane.showMessageDialog(null,e);
            }
        }finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
    public void update_user(User user){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("UPDATE User SET pass = ?,smoke_frequency_per_week = ?, alcool_frequency_per_week = ?, "
                    + "exercise_frequency_per_week = ?,height = ?,sexo = ?, email = ?, url_foto = ?, data_nascimento = ?, name = ? WHERE login = ? ");
            
            stmt.setString(1, user.getPass());
            stmt.setInt(2, user.getSmoke_frequency_per_week());
            stmt.setInt(3,user.getAlcool_frequency_per_week());
            stmt.setInt(4,user.getExercise_frequency_per_week());
            stmt.setFloat(5,user.getHeight());
            stmt.setString(6,user.getSexo());
            stmt.setString(7, user.getEmail());
            stmt.setString(8,user.getUrl_foto());
            stmt.setDate(9,user.getData_nascimento());
            stmt.setString(10, user.getName());
            stmt.setString(11,user.getLogin());            
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Usuário atualizado com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
        
    }
    
    public void delete_user(User user){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            Day_atualizationDAO.delete_day_atualization_user(user);
            
            stmt = (PreparedStatement) con.prepareStatement("DELETE FROM User WHERE login = ?");
            stmt.setString(1, user.getLogin());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null,"Usuário excluído com Sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
        
    }
    
    public static boolean acesso(String login, String pass){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM User WHERE login = ?");
            stmt.setString(1,login);
            rs = stmt.executeQuery();
            
            if(rs.next()){
                if(pass.equals(rs.getString("pass"))){
                    return true;
                }else{
                    JOptionPane.showMessageDialog(null,"Senha Incorreta");
                    return false;
                }
            
            }else{
                JOptionPane.showMessageDialog(null,"Usuário não existe");
                return false;
            }
            
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return false;
    }
    
    public static User get_user(String login){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = new User();
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("SELECT *,"
                    + "YEAR(CURDATE()) - YEAR(data_nascimento) - (RIGHT(CURDATE(), 5) < RIGHT(data_nascimento, 5)) as Age "
                    + " FROM User WHERE login = ?");
            stmt.setString(1,login);
            rs = stmt.executeQuery();
            
            if(rs.next()){
                user.setLogin(rs.getString("login"));
                user.setPass(rs.getString("pass"));
                user.setName(rs.getString("name"));
                user.setSmoke_frequency_per_week(rs.getInt("smoke_frequency_per_week"));
                user.setAlcool_frequency_per_week(rs.getInt("alcool_frequency_per_week"));
                user.setExercise_frequency_per_week(rs.getInt("exercise_frequency_per_week"));
                user.setHeight(rs.getFloat("height"));
                user.setSexo(rs.getString("sexo"));
                user.setEmail(rs.getString("email"));
                user.setUrl_foto(rs.getString("url_foto"));
                user.setAge(rs.getInt("Age"));
                user.setData_nascimento(rs.getDate("data_nascimento"));

                
                return user;
            }else{
                JOptionPane.showMessageDialog(null,"Erro ao encontrar o usuário");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
      
       return user;
    }
    
    
}
