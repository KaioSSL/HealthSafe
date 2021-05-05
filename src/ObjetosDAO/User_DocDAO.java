/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjetosDAO;

import Objetos.ConnectionFactory;
import Objetos.User_Doc;
import com.mysql.jdbc.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author kaiof
 */
public class User_DocDAO extends Objetos.User_Doc {

    public User_DocDAO(String User_login, String Doctor_CRA) {
        super(User_login, Doctor_CRA);
    }

    public User_DocDAO() {

    }
    
    public void delete_user_doc(User_Doc user_doc){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("DELETE FROM User_doc WHERE User_login = ? AND Doctor_CRA = ?");
            stmt.setString(1,user_doc.getUser_login());
            stmt.setString(2,user_doc.getDoctor_CRA());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null,"Doutor deletado com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        
    }
    
    public void insert_user_doc(User_Doc user_doc){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM USER_DOC WHERE User_login = ? AND Doctor_CRA = ?");
            stmt.setString(1, user_doc.getUser_login());
            stmt.setString(2,user_doc.getDoctor_CRA());
            rs = stmt.executeQuery();
            if(!rs.next()){
                stmt = (PreparedStatement) con.prepareStatement("INSERT INTO User_Doc(Doctor_CRA,User_login) VALUES(?,?)");
                stmt.setString(1,user_doc.getDoctor_CRA());
                stmt.setString(2,user_doc.getUser_login());
                stmt.executeUpdate();
            }else{
                JOptionPane.showMessageDialog(null,"Já existe o cadastro com este Doutor");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt,rs);
        }

    }
    
    public void delete_user_doc_login(User_Doc user_doc){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("DELETE FROM User_doc WHERE User_login = ?");
            stmt.setString(1,user_doc.getUser_login());
            stmt.executeUpdate();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        
    }
    
    public void delete_user_doc_cra(User_Doc user_doc){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("DELETE FROM User_doc WHERE Doctor_CRA = ?");
            stmt.setString(1,user_doc.getDoctor_CRA());
            stmt.executeUpdate();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        
    }
    
    
}
