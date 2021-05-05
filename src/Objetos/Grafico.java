/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author kaiof
 */
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
public class Grafico {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public CategoryDataset createDataset_peso_imc(ArrayList<Day_atualization> day_a_list){
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for(Day_atualization day_a:day_a_list){
            dataSet.addValue(day_a.getIMC(), "IMC", this.sdf.format(day_a.getDay_atualization()));
            dataSet.addValue(day_a.getWeight(),"Peso",this.sdf.format(day_a.getDay_atualization()));
        }
        return dataSet;
    }
    public CategoryDataset createDataset_ps_pd(ArrayList<Day_atualization> day_a_list){
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for(Day_atualization day_a:day_a_list){
            dataSet.addValue(day_a.getSystolic_pressure(), "Pressão Systolica", this.sdf.format(day_a.getDay_atualization()));
            dataSet.addValue(day_a.getDistolic_pressure(),"Pressão Diastólica",this.sdf.format(day_a.getDay_atualization()));
        }
        return dataSet;
    }
    public JFreeChart createLineChart(CategoryDataset dataSet,String title,String yTitle){
        JFreeChart graficoLinhas = ChartFactory.createLineChart(title,
                "", 
                yTitle,
                dataSet,
                PlotOrientation.VERTICAL,
                true,
                false,
                false);
        graficoLinhas.setBorderVisible(false);
        //graficoLinhas.setBackgroundImage(Utilitarios.redimensionarFoto("src\\images\\ocean.jpg").getImage());
        graficoLinhas.setBackgroundPaint(new Color(255,255,255));
        return graficoLinhas;
    }
    
    public ChartPanel create_line_chart_weight_imc(ArrayList<Day_atualization> day_a_list){
        CategoryDataset dataSet = createDataset_peso_imc(day_a_list);
        
        JFreeChart grafico = createLineChart(dataSet,"IMC x Peso","IMC");
        grafico.setBorderVisible(false);
        
        ChartPanel painelGrafico = new ChartPanel(grafico);
        painelGrafico.setPreferredSize(new Dimension(460,120));
        
        return painelGrafico;
    
    }

    public ChartPanel create_line_chart_ps_pd(ArrayList<Day_atualization> day_a_list){
        CategoryDataset dataSet = createDataset_ps_pd(day_a_list);
        
        JFreeChart grafico = createLineChart(dataSet,"PSxPD","PSxPD");
        
        ChartPanel painelGrafico = new ChartPanel(grafico);
        painelGrafico.setPreferredSize(new Dimension(460,120));
        
        return painelGrafico;
    }
}
