/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;
import ObjetosDAO.DoctorDAO;
import java.util.*;
import javax.swing.JOptionPane;
/**
 *
 * @author kaiof
 */
public class Day_atualization {
    private Date day_atualization;
    private float systolic_pressure;
    private float distolic_pressure;
    private float weight;
    private boolean alcool;
    private boolean smoke;
    private boolean exercise;
    private boolean fast_food;
    private String classification_pressure;
    private int day_tip;
    private String userLogin;
    private String classification_imc;
    private float IMC;
    private int idDay_Atualization;

    public Day_atualization(Date day_atualization, float systolic_pressure, float distolic_pressure, float weight, boolean alcool, boolean smoke, boolean exercise, boolean fast_food, String classification_pressure, int day_tip, String userLogin, String classification_imc, float IMC, int idDay_Atualization) {
        this.day_atualization = day_atualization;
        this.systolic_pressure = systolic_pressure;
        this.distolic_pressure = distolic_pressure;
        this.weight = weight;
        this.alcool = alcool;
        this.smoke = smoke;
        this.exercise = exercise;
        this.fast_food = fast_food;
        this.classification_pressure = classification_pressure;
        this.day_tip = day_tip;
        this.userLogin = userLogin;
        this.classification_imc = classification_imc;
        this.IMC = IMC;
        this.idDay_Atualization = idDay_Atualization;
    }

    public int getIdDay_Atualization() {
        return idDay_Atualization;
    }

    public void setIdDay_Atualization(int idDay_Atualization) {
        this.idDay_Atualization = idDay_Atualization;
    }

    public float getIMC() {
        return IMC;
    }

    public void setIMC(float IMC) {
        this.IMC = IMC;
    }

    public String getClassification_pressure() {
        return classification_pressure;
    }

    public void setClassification_pressure(String classification_pressure) {
        this.classification_pressure = classification_pressure;
    }

    public String getClassification_imc() {
        return classification_imc;
    }

    public void setClassification_imc(String classification_imc) {
        this.classification_imc = classification_imc;
    }
 
    public Day_atualization(){
    
    }

    public Date getDay_atualization() {
        return day_atualization;
    }

    public void setDay_Atualization(Date idDay_Atualization) {
        this.day_atualization = idDay_Atualization;
    }

    public float getSystolic_pressure() {
        return systolic_pressure;
    }

    public void setSystolic_pressure(float systolic_pressure) {
        this.systolic_pressure = systolic_pressure;
    }

    public float getDistolic_pressure() {
        return distolic_pressure;
    }

    public void setDiastolic_pressure(float blood_pressure) {
        this.distolic_pressure = blood_pressure;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean isAlcool() {
        return alcool;
    }

    public void setAlcool(boolean alcool) {
        this.alcool = alcool;
    }

    public boolean isSmoke() {
        return smoke;
    }

    public void setSmoke(boolean smoke) {
        this.smoke = smoke;
    }

    public boolean isExercise() {
        return exercise;
    }

    public void setExercise(boolean exercise) {
        this.exercise = exercise;
    }

    public boolean isFast_food() {
        return fast_food;
    }

    public void setFast_food(boolean fast_food) {
        this.fast_food = fast_food;
    }
    
    public int getDay_tip() {
        return day_tip;
    }

    public void setDay_tip(int day_tip) {
        this.day_tip = day_tip;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
    
    public static String classification_pressure(float systolic,float diastolic){
        if(systolic < 120 && diastolic < 80){
            return "Ótima";
        }else if(systolic < 130 && diastolic <85){
            return "Normal";
        }else if((systolic >= 130 && systolic <= 139)&&(diastolic >= 85 && diastolic <= 89)){
            return "Limítrofe";
        }else if((systolic >= 140 && systolic <= 159)&&(diastolic >= 90 && diastolic<=99 )){
            return "Hipertensão estágio 1";
        }else if((systolic>=160 && systolic<=179)&&(diastolic>=100 && diastolic<=109)){
            return "Hipertensão estágio 2";
        }else if(systolic >=140 && diastolic<90){
            return "Hipertensão sistólica isolada";
        }else{
            return "Hipertensão estágio 3";
        }
    }
            
    public static String classification_imc(float imc){
        if(imc<18.5){
            return "Abaixo do peso";
        }else if(imc>=18 && imc<25){
            return "Peso normal";
        }else if(imc>=25 && imc <30){
            return "Sobrepeso";
        }else if(imc>=30 && imc<35){
            return "Obesidade grau 1";
        }else if(imc>=35 && imc<40){
            return "Obesidade grau 2";
        }else{
            return "Obesidade grau 3";
        }
    }
    
    public static int classification_pressureInt(float systolic,float diastolic){
        if(systolic < 120 && diastolic < 80){
            return 1;
        }else if(systolic < 130 && diastolic <85){
            return 2;
        }else if((systolic >= 130 && systolic <= 139)&&(diastolic >= 85 && diastolic <= 89)){
            return 3;
        }else if((systolic >= 140 && systolic <= 159)&&(diastolic >= 90 && diastolic<=99 )){
            return 4;
        }else if((systolic>=160 && systolic<=179)&&(diastolic>=100 && diastolic<=109)){
            return 5;
        }else if(systolic >=140 && diastolic<90){
            return 6;
        }else{
            return 7;
        }
    }
    
    public static void doctorEmail(User user,Day_atualization day_a){
        if(classification_pressureInt(day_a.getSystolic_pressure(),day_a.getDistolic_pressure())>3){
            ArrayList<Doctor> doctor_list = DoctorDAO.get_doctor(user.getLogin());
            if(!doctor_list.isEmpty()){
                Email email = new Email();
                email.setTitle("HealthSafe - Atualização - "+user.getName());
                email.setMessage(Email.buildMessage(user, day_a));
                for(Doctor doc : doctor_list){
                    email.setDestinatario(doc.getEmail());
                    Email.sendEmail(email);
                }
            }
            
        }
        
    
    }
}
