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
public class Doctor {
    private String CRA;
    private String nome;
    private String email;

    public Doctor(String CRA, String nome, String email) {
        this.CRA = CRA;
        this.nome = nome;
        this.email = email;
    }

    public Doctor() {
    }
    

    public String getCRA() {
        return CRA;
    }

    public void setCRA(String CRA) {
        this.CRA = CRA;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
