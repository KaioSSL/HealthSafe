/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Label;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author kaiof
 */
public class Utilitarios {
    
    public static int indiceSemanal(String boxText){
        int indice = 0;
        if(boxText.equals("2-4 Dias")){
              return indice = 1;
          }else if(boxText.equals("4+ Dias")){
              return indice = 2;
          }
        return indice;
    }
    
    public static void changeBackground_entered(JPanel panel){
        panel.setBackground(new Color(236,236,255));
    }
    public static void changeBackground_exited(JPanel panel){
        panel.setBackground(new Color(214,214,252));
    }
    public static void changeForeground_entered(JLabel label){
        label.setForeground(new Color(106,106,208));
    }
    public static void changeForeground_exited(JLabel label){
        label.setForeground(new Color(0,0,51));
    }
    public static void resetColor(JPanel panel){
        Component[] com = panel.getComponents();
        for(Component con : com){
            con.setBackground(new Color(129,129,209));
        }
    }
    
    public static String buscarFoto(JLabel foto){
        //Cria o file choser
        JFileChooser fileChooser = new JFileChooser();
        //Permite que o usuario apenas selecione arquivos e nao pastas
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //Mostra o File Chosser em cima de alguem
        int seletorAberto = fileChooser.showOpenDialog(null);
        //Filtra as extensoes que aparecerao
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Foto",
                "jpg","png","jpeg","jfif");
        //Implementa dentro da função o filtro definido a cima
        fileChooser.setFileFilter(filter);
        //Coloca titulo do file Chooser
        fileChooser.setDialogTitle("Buscar Foto");
        Path path = FileSystems.getDefault().getPath("src\\Images\\fotoUser200.png");
        String caminhoDefault = path.toString();
        
        if(seletorAberto == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            foto.setIcon(redimensionarFoto(file.getPath(),foto));
            caminhoDefault = file.getPath();
        }
        else{
            if(seletorAberto == JFileChooser.CANCEL_OPTION){
                ImageIcon imgDefault;
                imgDefault = new ImageIcon(caminhoDefault);
                foto.setIcon(imgDefault);
                

            }
        
        }
        return caminhoDefault;
    }
    
    public static ImageIcon redimensionarFoto(String caminho,JLabel recipiente){
        ImageIcon img = new ImageIcon(caminho);
        Image image = img.getImage();
        Image image2 = image.getScaledInstance(recipiente.getWidth(), recipiente.getHeight(), Image.SCALE_DEFAULT);
        ImageIcon imgFinal = new ImageIcon(image2);
        return imgFinal;
    }
    public static ImageIcon redimensionarFoto(String caminho){
        ImageIcon img = new ImageIcon(caminho);
        Image image = img.getImage();
        Image image2 = image.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        ImageIcon imgFinal = new ImageIcon(image2);        
        return imgFinal;
    }
    public static void fotoDefault(JLabel fotoMembro){
           ImageIcon imgDefault;
           Path path = FileSystems.getDefault().getPath("src\\Images\\fotoUser200.png");
           String caminhoDefault = path.toString();
           imgDefault = new ImageIcon(caminhoDefault);
           fotoMembro.setIcon(imgDefault);
    }
    
    public static String copiarFoto(String caminho_foto,String nome) throws IOException{
        FileChannel source = null;
        FileChannel destination = null;
        
        try {
            source = new FileInputStream(caminho_foto).getChannel();
            destination = new FileOutputStream("usersImages\\"+nome+".png").getChannel();
            source.transferTo(0, source.size(), destination);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }finally{
            if(source != null && source.isOpen())
                source.close();
            if(destination !=null && destination.isOpen())
                destination.close();
        }
        
        
        return "usersImages\\"+nome+".png";
    
    }
    
    public static boolean validarCPF(String cpf){
        String cpf2 = cpf.replace(".", "");
        int cont = 10;
        int soma = 0;
        String c1 = "";
        String c2 = "";
        //Primeira validação
        for(int i = 0;i<9;i++){
            soma += cont * Integer.parseInt(String.valueOf(cpf2.charAt(i)));
            cont = cont -1;
        }
        if(11 - (soma%11)>9){
            c1 ="0";
        }else{
            c1 = String.valueOf(11 - (soma%11));
        }
        cont = 11;
        cpf2 = cpf2.replace("-", "");
        soma = 0;
        for(int i = 0;i<10;i++){
            soma += cont * Integer.parseInt(String.valueOf(cpf2.charAt(i)));
            cont = cont -1;
        }
        
        if(11 - soma%11 > 9){
            c2 = "0";
        }else{
            c2 = String.valueOf(11 - (soma%11));
        }
        if(c1.equals(String.valueOf(cpf2.charAt(cpf2.length()-2))) && c2.equals(String.valueOf(cpf2.charAt(cpf2.length()-1))) ){
            return true;
        }
        else{
            return false;
        }
    }
    
    private static boolean validarData(String data){
        String data2 = data.replace("/","");
        int dia = Integer.parseInt(data2.substring(0, 2));
        int mes = Integer.parseInt(data2.substring(2,4));
        int ano  = Integer.parseInt(data2.substring(4));
        
        if(mes>12 || dia > 31){
            return false;
        }else{
        if(ano%4!=0 && mes == 2 && dia > 28){
            return false;
        }else{
        if( (mes == 4 && dia>31) || (mes == 6 && dia>31) || (mes == 9 && dia>31) || (mes == 11 && dia>31)){
                    return false;
                }else{
                    return true;
                }
            }
        }
    }
    
    public static boolean validarEmail(String email){
        int cont1 = 0;
        int cont2 = 0;
        String a = "@";
        String p = ".";
        for(int i =0;i<email.length();i++){
           
            if(a.equals(String.valueOf(email.charAt(i)))){
                
                cont1++;

            }
            else if(String.valueOf(email.charAt(i)).equals(p)){
                cont2++;
            }
        }
        if(cont1 == 1 && cont2>0){
            return true;
        }else{
            JOptionPane.showMessageDialog(null,"Informe um email válido");
            return false;
        }
        
    } 
    
    public static boolean validarCampos(JPanel panel){
        Component[] campos = panel.getComponents();
        for(int i = 0;i<campos.length;i++){
                if(campos[i] instanceof JFormattedTextField){
                    JFormattedTextField campo = (JFormattedTextField) campos[i];
                    if(campo.getText().length()==14){
                        String cpf = campo.getText().replace(" ", "");
                        if(cpf.length()<14){
                            return false;
                            
                        }else{
                            if(!validarCPF(cpf)){
                                JOptionPane.showMessageDialog(null,"Preencha com CPF válido");
                                return false;
                            }
                        }
                    }
                    else{
                        if(campo.getText().length()==10){
                            String data = campo.getText().replace(" ", "");
                            if(data.length()<10){
                                return false;
                            }else{
                                if(!validarData(data)){
                                    JOptionPane.showMessageDialog(null, "Preencha com Datas Válidas");
                                    return false;
                                }
                            }
                        }
                    else{
                        if(campo.getText().length()==12){
                            String rg = campo.getText().replace(" ", "");
                            if(rg.length()<12){
                                return false;
                            }
                        }
                    else{
                        if(campo.getText().length()==16){
                            String telefone = campo.getText().replace(" ","");
                            if(telefone.length()<14){
                                return false;
                            }
                        }
                    }
                    }
                    }
                }
                else{
                    if(campos[i] instanceof JTextField){
                        JTextField campo = (JTextField) campos[i];
                        if("".equals(campo.getText())){
                            JOptionPane.showMessageDialog(null,"Não deixe Campos Vazios");
                            return false;
                        }
                    }
                }
            }
        
    return true;
    }
    
    public static void limparField(JPanel panel){
        Component[] campos = panel.getComponents();
        for(int i =0;i<campos.length;i++){
            if(campos[i] instanceof JTextField){
                ((JTextField)campos[i]).setText("");
            }else{
                if(campos[i] instanceof JTextArea){
                    ((JTextArea)campos[i]).setText("");
                }else if(campos[i] instanceof JPanel){
                    limparField((JPanel) campos[i]);
                    }      
            }
        }
    }
    
    public static void  atualizarPainel(JPanel panelPai, JPanel filho){
        panelPai.removeAll();
        panelPai.revalidate();
        panelPai.repaint();
        
        panelPai.add(filho);
        panelPai.revalidate();
        panelPai.repaint();
        
    
    
    }
    
    public static boolean trueorfalse(String string){
        if(string.equals("Sim")){
            return true;
        }else{
        return false;
        }
    }
}
