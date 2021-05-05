/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjetosDAO;

import Objetos.ConnectionFactory;
import Objetos.Doctor;
import Objetos.User_Doc;
import com.mysql.jdbc.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author kaiof
 */
public class DoctorDAO extends Objetos.Doctor {

    public DoctorDAO(String CRA, String nome, String email) {
        super(CRA, nome, email);
    }

    public DoctorDAO() {
    }
    
    
    
    public void insert_doctor(Doctor doctor,String user_login){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("INSERT INTO Doctor VALUES(?,?,?)");
            stmt.setString(1,doctor.getCRA());
            stmt.setString(2,doctor.getNome());
            stmt.setString(3,doctor.getEmail());

            stmt.executeUpdate();

            User_DocDAO ud = new User_DocDAO(user_login,doctor.getCRA());
            ud.insert_user_doc(ud);

            JOptionPane.showMessageDialog(null,"Doutor registrado com sucesso");
            
        } catch (SQLException ex) {
            if(ex.getErrorCode() == 1062){
                User_DocDAO ud = new User_DocDAO(user_login,doctor.getCRA());
                ud.insert_user_doc(ud);

            }
        }finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
    public void update_doctor(Doctor doctor){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("UPDATE Doctor SET nome = ?, email = ? WHERE CRA = ?");
            stmt.setString(1, doctor.getNome());
            stmt.setString(2, doctor.getEmail());
            stmt.setString(3,doctor.getCRA());
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null,"Doutor alterado com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
    public void delete_doctor(Doctor doctor){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            User_DocDAO ud = new User_DocDAO(null,doctor.getCRA());
            ud.delete_user_doc_cra(ud);
            
            stmt = (PreparedStatement) con.prepareStatement("DELETE FROM Doctor WHERE CRA = ?");
            stmt.setString(1,doctor.getCRA());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DoctorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static ArrayList<Doctor> get_doctor(String login){
        Connection con = (Connection) ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Doctor> doctor_list = new ArrayList<>();
        try {
            stmt = (PreparedStatement) con.prepareStatement("SELECT doc.* FROM User_doc ud INNER JOIN Doctor doc ON(ud.doctor_Cra = doc.CRA) WHERE ud.User_login = ?");
            stmt.setString(1, login);
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Doctor doc = new Doctor();
                doc.setCRA(rs.getString("doc.CRA"));
                doc.setEmail(rs.getString("doc.email"));
                doc.setNome(rs.getString("doc.nome"));
                doctor_list.add(doc);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DoctorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt,rs);
        }
        
        return doctor_list;
    }
}
