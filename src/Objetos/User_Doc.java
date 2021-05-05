/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

/**
 *
 * @author kaiof
 */
public class User_Doc {
    private int idUser_doc;
    private String User_login;
    private String Doctor_CRA;

    public User_Doc(String User_login, String Doctor_CRA) {
        this.User_login = User_login;
        this.Doctor_CRA = Doctor_CRA;
    }

    public User_Doc() {
        
    }
    
    

    public int getIdUser_doc() {
        return idUser_doc;
    }

    public void setIdUser_doc(int idUser_doc) {
        this.idUser_doc = idUser_doc;
    }

    public String getUser_login() {
        return User_login;
    }

    public void setUser_login(String User_login) {
        this.User_login = User_login;
    }

    public String getDoctor_CRA() {
        return Doctor_CRA;
    }

    public void setDoctor_CRA(String Doctor_CRA) {
        this.Doctor_CRA = Doctor_CRA;
    }
    
    
}
