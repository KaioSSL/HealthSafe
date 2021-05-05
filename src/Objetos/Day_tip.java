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
public class Day_tip {
    private int idDay_tip;
    private String day_tip_text;
    private String day_tip_title;
    private String url_image;

    public Day_tip(int idDay_tip, String day_tip_text, String day_tip_title, String url_image) {
        this.idDay_tip = idDay_tip;
        this.day_tip_text = day_tip_text;
        this.day_tip_title = day_tip_title;
        this.url_image = url_image;
    }

    public Day_tip() {
    }
    

    public int getIdDay_tip() {
        return idDay_tip;
    }

    public void setIdDay_tip(int idDay_tip) {
        this.idDay_tip = idDay_tip;
    }

    public String getDay_tip_text() {
        return day_tip_text;
    }

    public void setDay_tip_text(String day_tip_text) {
        this.day_tip_text = day_tip_text;
    }

    public String getDay_tip_title() {
        return day_tip_title;
    }

    public void setDay_tip_title(String day_tip_title) {
        this.day_tip_title = day_tip_title;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }
    
    public static int get_day_tip(Day_atualization day_a){
        if(day_a.isSmoke()){
            return 5;
        }else if(day_a.isAlcool()){
            return 4;
        }else if((!day_a.getClassification_pressure().equals("Ótima"))&&(!day_a.getClassification_pressure().equals("Normal"))){
            return 2;
        }else if(day_a.getClassification_imc().equals("Obesidade grau 1")){
            return 3;
        }else if(!day_a.getClassification_imc().equals("Peso normal")){
            return 1;
        }else{
            return 6;
        }
    }
    
    
}
