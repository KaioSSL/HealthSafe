/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import java.sql.Date;

/**
 *
 * @author kaiof
 */
public class User {
    private String login;
    private String pass;
    private String name;
    private int smoke_frequency_per_week;
    private int alcool_frequency_per_week;
    private int exercise_frequency_per_week;
    private float height;
    private String sexo;
    private String email;
    private String url_foto;
    private Date data_nascimento;
    private Date data_cadastro;
    private int age;

    public User() {
    }

    public User(String login, String pass, String name, int smoke_frequency_per_week, int alcool_frequency_per_week, int exercise_frequency_per_week, float height, String sexo, String email, String url_foto, Date data_nascimento, Date data_cadastro) {
        this.login = login;
        this.pass = pass;
        this.name = name;
        this.smoke_frequency_per_week = smoke_frequency_per_week;
        this.alcool_frequency_per_week = alcool_frequency_per_week;
        this.exercise_frequency_per_week = exercise_frequency_per_week;
        this.height = height;
        this.sexo = sexo;
        this.email = email;
        this.url_foto = url_foto;
        this.data_nascimento = data_nascimento;
        this.data_cadastro = data_cadastro;
    }

    public Date getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(Date data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public Date getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(Date data_cadastro) {
        this.data_cadastro = data_cadastro;
    }
   

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSmoke_frequency_per_week() {
        return smoke_frequency_per_week;
    }

    public void setSmoke_frequency_per_week(int smoke_frequency_per_week) {
        this.smoke_frequency_per_week = smoke_frequency_per_week;
    }

    public int getAlcool_frequency_per_week() {
        return alcool_frequency_per_week;
    }

    public void setAlcool_frequency_per_week(int alcool_frequency_per_week) {
        this.alcool_frequency_per_week = alcool_frequency_per_week;
    }

    public int getExercise_frequency_per_week() {
        return exercise_frequency_per_week;
    }

    public void setExercise_frequency_per_week(int exercise_frequency_per_week) {
        this.exercise_frequency_per_week = exercise_frequency_per_week;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    
}
