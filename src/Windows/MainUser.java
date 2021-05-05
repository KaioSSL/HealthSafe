/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windows;

import Objetos.Day_atualization;
import Objetos.Day_tip;
import Objetos.Doctor;
import Objetos.Grafico;
import Objetos.User;
import Objetos.Utilitarios;
import ObjetosDAO.Day_atualizationDAO;
import ObjetosDAO.Day_tipDAO;
import ObjetosDAO.DoctorDAO;
import ObjetosDAO.UserDAO;
import ObjetosDAO.User_DocDAO;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author kaiof
 */
public class MainUser extends javax.swing.JFrame {
    private User user;
    private int count_atualization;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Day_tip day_tip;
    String caminho_foto = "src\\Images\\fotoUser200.png";
    /**
     * Creates new form MainUser
     */
    public MainUser(User user) {
        initComponents();
        this.user = user;
        Utilitarios.atualizarPainel(panel_desk, panel_desk_main);
        this.count_atualization = Day_atualizationDAO.day_atualization_count(user.getLogin());
        if(this.count_atualization < 1){
            Utilitarios.atualizarPainel(panel_desk, panel_avaliacao_diaria);
            JOptionPane.showMessageDialog(null,"Bem vindo ao HealthSafe, registre sua primeira avaliação diária");
        }  
        atualizarSistema();
    }
    
    
    private void dica_do_dia(){
        Pop_dica pop = new Pop_dica(this.day_tip.getUrl_image(),this.day_tip.getDay_tip_text(),this.day_tip.getDay_tip_title());
        pop.setVisible(true);
    }
    private void dica_do_dia(Date data){
        Pop_dica pop = new Pop_dica(this.day_tip.getUrl_image(),"A dica do dia " + this.sdf.format(data) + " foi: " +this.day_tip.getDay_tip_text(),this.day_tip.getDay_tip_title());
        pop.setVisible(true);
        
    }
    private void atualizarCamposAUsuario(){
        f_alogin.setText(f_plogin.getText());
        f_anome.setText(f_pnome.getText());
        f_aaltura.setText(f_paltura.getText());
        f_asenha_nova.setText(f_psenha.getText());
        f_aemail.setText(f_pemail.getText());
        f_adata.setText(f_pdata.getText());
        box_afumos.setSelectedIndex(box_fumos.getSelectedIndex());
        box_aalcool.setSelectedIndex(box_alcool.getSelectedIndex());
        box_aexercise.setSelectedIndex(box_exercise.getSelectedIndex());
        box_asexo.setSelectedItem(box_psexo.getSelectedItem());
        l_afoto.setIcon(l_pfoto.getIcon());
    }
    
    private void atualizarCamposUsuario(){
        f_plogin.setText(user.getLogin());
        f_pnome.setText(user.getName());
        f_pemail.setText(user.getEmail());
        f_psenha.setText(user.getPass());
        f_paltura.setText(String.valueOf(user.getHeight()));
        f_pdata.setText(sdf.format(user.getData_nascimento()));
        box_fumos.setSelectedIndex(user.getSmoke_frequency_per_week());
        box_alcool.setSelectedIndex(user.getAlcool_frequency_per_week());
        box_exercise.setSelectedIndex(user.getExercise_frequency_per_week());
        box_asexo.setSelectedItem(user.getSexo());
        l_pfoto.setIcon(Utilitarios.redimensionarFoto(user.getUrl_foto(), l_pfoto));
    }
    
    private void atualizarSistema(){
        label_nome.setText(user.getName());
        label_idade.setText(String.valueOf(user.getAge()) + " Anos");
        label_altura.setText(String.valueOf(user.getHeight()) + " cm");
        label_foto.setIcon(Utilitarios.redimensionarFoto(user.getUrl_foto(), label_foto));
        Day_atualization recent_day_a = Day_atualizationDAO.get_recent_day_atualization(this.user.getLogin());
        label_ps_recente.setText(String.valueOf(recent_day_a.getSystolic_pressure()));
        label_pd_recente.setText(String.valueOf(recent_day_a.getDistolic_pressure()));
        label_peso_recente.setText(String.valueOf(recent_day_a.getWeight()));
        label_imc_recente.setText(String.valueOf(recent_day_a.getIMC()));
        label_class_pressao_recente.setText(recent_day_a.getClassification_pressure());
        label_class_imc_recente.setText(recent_day_a.getClassification_imc());
        this.day_tip = Day_tipDAO.get_day_tip(recent_day_a.getDay_tip());
        this.caminho_foto = user.getUrl_foto();
        atualizar_graficos_desk_main();
        atualizarTabelas();
        
    }
    private void atualizar_graficos_desk_main(){
        Grafico grafico = new Grafico();
        panel_grafico_peso_imc.setLayout(new BorderLayout());
        Utilitarios.atualizarPainel(panel_grafico_peso_imc, grafico.create_line_chart_weight_imc(Day_atualizationDAO.get_day_atualization(this.user.getLogin())));
        panel_grafico_ps_pd.setLayout(new BorderLayout());
        Utilitarios.atualizarPainel(panel_grafico_ps_pd,grafico.create_line_chart_ps_pd(Day_atualizationDAO.get_day_atualization(this.user.getLogin())));
    }
    private void atualizar_graficos_progressao_imc_peso(){
        Grafico grafico = new Grafico();
        panel_grafico_peso_imc_datado.setLayout(new BorderLayout());
        java.sql.Date data_inicio = null;
        java.sql.Date data_fim = null;
        try {
            if(!f_data_inicio_grafico_peso_imc.getText().replaceAll("/","").replaceAll(" ","").equals("")){
                data_inicio = new java.sql.Date(this.sdf.parse(f_data_inicio_grafico_peso_imc.getText()).getTime());
                JOptionPane.showMessageDialog(null,data_inicio);
            }
            if(!f_data_fim_grafico_peso_imc.getText().replaceAll("/","").replaceAll(" ","").equals("")){
                data_fim = new java.sql.Date(this.sdf.parse(f_data_fim_grafico_peso_imc.getText()).getTime());
                JOptionPane.showMessageDialog(null,data_fim);
            }
        } catch (ParseException ex) {
            Logger.getLogger(MainUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        Utilitarios.atualizarPainel(panel_grafico_peso_imc_datado, grafico.create_line_chart_weight_imc(Day_atualizationDAO.get_day_atualization(this.user.getLogin(),data_inicio,data_fim)));
        //Utilitarios.limparField(panel_progressao_peso_imc);
    }
    private void atualizar_graficos_progressao_ps_pd(){
        Grafico grafico = new Grafico();
        panel_grafico_ps_pd_datado.setLayout(new BorderLayout());
        java.sql.Date data_inicio = null;
        java.sql.Date data_fim = null;
        try {
            if(!f_data_inicio_grafico_ps_pd.getText().replaceAll("/","").replaceAll(" ","").equals("")){
                data_inicio = new java.sql.Date(this.sdf.parse(f_data_inicio_grafico_ps_pd.getText()).getTime());
            }
            if(!f_data_fim_grafico_ps_pd.getText().replaceAll("/","").replaceAll(" ","").equals("")){
                data_fim = new java.sql.Date(this.sdf.parse(f_data_fim_grafico_ps_pd.getText()).getTime());
            }
        } catch (ParseException ex) {
            Logger.getLogger(MainUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        Utilitarios.atualizarPainel(panel_grafico_ps_pd_datado, grafico.create_line_chart_ps_pd(Day_atualizationDAO.get_day_atualization(this.user.getLogin(),data_inicio,data_fim)));
        Utilitarios.limparField(panel_progressao_ps_pd);
    
    
    }
    
    private void atualizarTabelas(){
        DefaultTableModel modeloMedicos = (DefaultTableModel) tb_medicos.getModel();
        tb_medicos.setRowSorter(new TableRowSorter(modeloMedicos));
        modeloMedicos.setNumRows(0);
        for(Doctor doc : DoctorDAO.get_doctor(this.user.getLogin())){
            modeloMedicos.addRow(new Object[]{
                doc.getNome(),
                doc.getCRA(),
                doc.getEmail()
            });
            
        }
    
    }
    private void get_dados_day_calendar_peso_imc(Date data){
        Day_atualization day_a = Day_atualizationDAO.get_day_atualization(this.user.getLogin(), data);
        if(day_a != null){
            f_data_calendar_peso_imc.setText(this.sdf.format(day_a.getDay_atualization()));
            f_imc_calendar.setText(String.valueOf(day_a.getIMC()));
            f_peso_calendar.setText(String.valueOf(day_a.getWeight()));
            this.day_tip = Day_tipDAO.get_day_tip(day_a.getDay_tip());
        }else{
            JOptionPane.showMessageDialog(null,"Não existe avalição neste dia");
            Utilitarios.limparField(panel_progressao_peso_imc);
        }
    }
    private void get_dados_day_calendar_ps_pd(Date data){
        Day_atualization day_a = Day_atualizationDAO.get_day_atualization(this.user.getLogin(), data);
        if(day_a != null){
            f_data_calendar_ps_pd.setText(this.sdf.format(day_a.getDay_atualization()));
            f_ps_calendar.setText(String.valueOf(day_a.getSystolic_pressure()));
            f_pd_calendar.setText(String.valueOf(day_a.getDistolic_pressure()));
            this.day_tip =Day_tipDAO.get_day_tip(day_a.getDay_tip());
        }else{
            JOptionPane.showMessageDialog(null,"Não existe avalição neste dia");
            Utilitarios.limparField(panel_progressao_ps_pd);
        }
    
    }

    private MainUser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        panel_main = new javax.swing.JPanel();
        panel_head = new javax.swing.JPanel();
        panel_btn_medicos = new javax.swing.JPanel();
        label_registrar_medico = new javax.swing.JLabel();
        panel_btn_avaliacao_diaria = new javax.swing.JPanel();
        label_avaliacao_diaria = new javax.swing.JLabel();
        panel_btn_informativo = new javax.swing.JPanel();
        label_informativo = new javax.swing.JLabel();
        panel_btn_sair = new javax.swing.JPanel();
        label_sair = new javax.swing.JLabel();
        panel_labels_head = new javax.swing.JPanel();
        label_foto = new javax.swing.JLabel();
        label_nome = new javax.swing.JLabel();
        label_idade = new javax.swing.JLabel();
        label_altura = new javax.swing.JLabel();
        panel_btn_home = new javax.swing.JPanel();
        label_home = new javax.swing.JLabel();
        panel_btn_conta = new javax.swing.JPanel();
        label_home1 = new javax.swing.JLabel();
        panel_desk = new javax.swing.JPanel();
        panel_desk_main = new javax.swing.JPanel();
        panel_desk_main_head = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        label_ps_recente = new javax.swing.JLabel();
        label_pd_recente = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        label_imc_recente = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        label_peso_recente = new javax.swing.JLabel();
        label555 = new javax.swing.JLabel();
        label_class_pressao_recente = new javax.swing.JLabel();
        label_class_imc_recente = new javax.swing.JLabel();
        label_classification_imc = new javax.swing.JLabel();
        panel_desk_main_desk = new javax.swing.JPanel();
        panel_btn_progressao_peso_imc = new javax.swing.JPanel();
        label_title_chart_peso_imc = new javax.swing.JLabel();
        panel_grafico_peso_imc = new javax.swing.JPanel();
        panel_btn_progressao_ps_pd = new javax.swing.JPanel();
        label_title_chart_ps_pd = new javax.swing.JLabel();
        panel_grafico_ps_pd = new javax.swing.JPanel();
        panel_progressao_peso_imc = new javax.swing.JPanel();
        panel_progressao_peso_imc_conteiner_chart = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        f_data_inicio_grafico_peso_imc = new javax.swing.JFormattedTextField();
        f_data_fim_grafico_peso_imc = new javax.swing.JFormattedTextField();
        panel_grafico_peso_imc_datado = new javax.swing.JPanel();
        btn_gerar_grafico_peso_imc = new javax.swing.JButton();
        panel_calendar_data = new javax.swing.JPanel();
        f_imc_calendar = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        f_data_calendar_peso_imc = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        btn_dica_do_dia_peso_imc = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        f_peso_calendar = new javax.swing.JTextField();
        calendar_peso_imc = new com.toedter.calendar.JCalendar();
        btn_gerar_dados_peso_imc = new javax.swing.JButton();
        brn_limpar_panel_peso_imc = new javax.swing.JButton();
        panel_progressao_ps_pd = new javax.swing.JPanel();
        panel_progressao_ps_pd_conteiner_chart = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        panel_grafico_ps_pd_datado = new javax.swing.JPanel();
        f_data_inicio_grafico_ps_pd = new javax.swing.JFormattedTextField();
        f_data_fim_grafico_ps_pd = new javax.swing.JFormattedTextField();
        btn_gerar_grafico_ps_pd = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        panel_calendar_ps_pd_data = new javax.swing.JPanel();
        calendar_ps_pd = new com.toedter.calendar.JCalendar();
        f_data_calendar_ps_pd = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        f_ps_calendar = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        f_pd_calendar = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        btn_gerar_dados_ps_pd = new javax.swing.JButton();
        btn_dica_do_dia_ps_pd = new javax.swing.JButton();
        brn_limpar_progressao_ps_pd = new javax.swing.JButton();
        panel_registrar_medico = new javax.swing.JPanel();
        label_registro_medico = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        f_CRA = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        f_nome_medico = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        f_email_medico = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btn_registrar_medico = new javax.swing.JButton();
        btn_voltar_medico = new javax.swing.JButton();
        f_senha_medico = new javax.swing.JPasswordField();
        panel_avaliacao_diaria = new javax.swing.JPanel();
        label_registro_medico1 = new javax.swing.JLabel();
        label_pressao_s_aval = new javax.swing.JLabel();
        f_systolic_pressurel = new javax.swing.JTextField();
        f_diastolic_pressure = new javax.swing.JTextField();
        label_pressao_d_aval = new javax.swing.JLabel();
        label_peso_aval = new javax.swing.JLabel();
        f_weight = new javax.swing.JTextField();
        label_peso_aval1 = new javax.swing.JLabel();
        label_peso_aval2 = new javax.swing.JLabel();
        label_peso_aval3 = new javax.swing.JLabel();
        label_peso_aval4 = new javax.swing.JLabel();
        label_peso_aval5 = new javax.swing.JLabel();
        box_smoke = new javax.swing.JComboBox<>();
        box_alcool = new javax.swing.JComboBox<>();
        box_exercise = new javax.swing.JComboBox<>();
        box_fastfood = new javax.swing.JComboBox<>();
        btn_registrar_aval = new javax.swing.JButton();
        btn_voltar_aval = new javax.swing.JButton();
        f_senha_aval = new javax.swing.JPasswordField();
        panel_medicos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_medicos = new javax.swing.JTable();
        btn_cadastrar_medico = new javax.swing.JButton();
        btn_excluir_medico = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        panel_perfil_usuario = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        l_pfoto = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        f_pdata = new javax.swing.JFormattedTextField();
        f_pemail = new javax.swing.JTextField();
        f_pnome = new javax.swing.JTextField();
        f_psenha = new javax.swing.JPasswordField();
        f_plogin = new javax.swing.JTextField();
        box_fumos = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        box_exercicios = new javax.swing.JComboBox<>();
        box_alcool1 = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        f_paltura = new javax.swing.JFormattedTextField();
        btn_palterar = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        box_psexo = new javax.swing.JComboBox<>();
        panel_atualizar_usuario = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        l_afoto = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        f_adata = new javax.swing.JFormattedTextField();
        f_aemail = new javax.swing.JTextField();
        f_anome = new javax.swing.JTextField();
        f_asenha_nova = new javax.swing.JPasswordField();
        f_alogin = new javax.swing.JTextField();
        box_afumos = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        box_aexercise = new javax.swing.JComboBox<>();
        box_aalcool = new javax.swing.JComboBox<>();
        jLabel42 = new javax.swing.JLabel();
        f_aaltura = new javax.swing.JFormattedTextField();
        btn_aalterar = new javax.swing.JButton();
        btn_alimpar = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();
        f_asenha = new javax.swing.JPasswordField();
        box_asexo = new javax.swing.JComboBox<>();
        jLabel47 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HealthSafe");
        setMinimumSize(new java.awt.Dimension(710, 520));
        setResizable(false);
        setSize(new java.awt.Dimension(710, 520));
        setType(java.awt.Window.Type.POPUP);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });

        panel_main.setBackground(new java.awt.Color(168, 168, 245));
        panel_main.setMaximumSize(new java.awt.Dimension(700, 520));
        panel_main.setMinimumSize(new java.awt.Dimension(700, 520));
        panel_main.setPreferredSize(new java.awt.Dimension(700, 520));
        panel_main.setRequestFocusEnabled(false);

        panel_head.setBackground(new java.awt.Color(214, 214, 252));
        panel_head.setForeground(new java.awt.Color(255, 51, 51));
        panel_head.setMaximumSize(new java.awt.Dimension(200, 500));
        panel_head.setMinimumSize(new java.awt.Dimension(200, 500));

        panel_btn_medicos.setBackground(new java.awt.Color(214, 214, 252));
        panel_btn_medicos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_btn_medicosMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel_btn_medicosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel_btn_medicosMouseExited(evt);
            }
        });

        label_registrar_medico.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        label_registrar_medico.setForeground(new java.awt.Color(255, 255, 255));
        label_registrar_medico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/doctor32.png"))); // NOI18N
        label_registrar_medico.setText("Doutores.");

        javax.swing.GroupLayout panel_btn_medicosLayout = new javax.swing.GroupLayout(panel_btn_medicos);
        panel_btn_medicos.setLayout(panel_btn_medicosLayout);
        panel_btn_medicosLayout.setHorizontalGroup(
            panel_btn_medicosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_btn_medicosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_registrar_medico, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_btn_medicosLayout.setVerticalGroup(
            panel_btn_medicosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label_registrar_medico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
        );

        panel_btn_avaliacao_diaria.setBackground(new java.awt.Color(214, 214, 252));
        panel_btn_avaliacao_diaria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_btn_avaliacao_diariaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel_btn_avaliacao_diariaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel_btn_avaliacao_diariaMouseExited(evt);
            }
        });

        label_avaliacao_diaria.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        label_avaliacao_diaria.setForeground(new java.awt.Color(255, 255, 255));
        label_avaliacao_diaria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/calendar32.png"))); // NOI18N
        label_avaliacao_diaria.setText("Avaliação Diária.");

        javax.swing.GroupLayout panel_btn_avaliacao_diariaLayout = new javax.swing.GroupLayout(panel_btn_avaliacao_diaria);
        panel_btn_avaliacao_diaria.setLayout(panel_btn_avaliacao_diariaLayout);
        panel_btn_avaliacao_diariaLayout.setHorizontalGroup(
            panel_btn_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_btn_avaliacao_diariaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_avaliacao_diaria, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
        );
        panel_btn_avaliacao_diariaLayout.setVerticalGroup(
            panel_btn_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label_avaliacao_diaria, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
        );

        panel_btn_informativo.setBackground(new java.awt.Color(214, 214, 252));
        panel_btn_informativo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_btn_informativoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel_btn_informativoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel_btn_informativoMouseExited(evt);
            }
        });

        label_informativo.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        label_informativo.setForeground(new java.awt.Color(255, 255, 255));
        label_informativo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/info32.png"))); // NOI18N
        label_informativo.setText("Informátivo.");

        javax.swing.GroupLayout panel_btn_informativoLayout = new javax.swing.GroupLayout(panel_btn_informativo);
        panel_btn_informativo.setLayout(panel_btn_informativoLayout);
        panel_btn_informativoLayout.setHorizontalGroup(
            panel_btn_informativoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_btn_informativoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_informativo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_btn_informativoLayout.setVerticalGroup(
            panel_btn_informativoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label_informativo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
        );

        panel_btn_sair.setBackground(new java.awt.Color(214, 214, 252));
        panel_btn_sair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_btn_sairMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel_btn_sairMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel_btn_sairMouseExited(evt);
            }
        });

        label_sair.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        label_sair.setForeground(new java.awt.Color(255, 255, 255));
        label_sair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout32.png"))); // NOI18N
        label_sair.setText("Sair.");

        javax.swing.GroupLayout panel_btn_sairLayout = new javax.swing.GroupLayout(panel_btn_sair);
        panel_btn_sair.setLayout(panel_btn_sairLayout);
        panel_btn_sairLayout.setHorizontalGroup(
            panel_btn_sairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_btn_sairLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_sair, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_btn_sairLayout.setVerticalGroup(
            panel_btn_sairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label_sair, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        panel_labels_head.setBackground(new java.awt.Color(214, 214, 252));

        label_foto.setBackground(new java.awt.Color(255, 255, 255));
        label_foto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/fotoUser128.png"))); // NOI18N

        label_nome.setBackground(new java.awt.Color(255, 255, 255));
        label_nome.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_nome.setForeground(new java.awt.Color(255, 255, 255));
        label_nome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_nome.setMaximumSize(new java.awt.Dimension(128, 17));
        label_nome.setMinimumSize(new java.awt.Dimension(128, 17));
        label_nome.setPreferredSize(new java.awt.Dimension(128, 17));

        label_idade.setBackground(new java.awt.Color(255, 255, 255));
        label_idade.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_idade.setForeground(new java.awt.Color(255, 255, 255));
        label_idade.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_idade.setMaximumSize(new java.awt.Dimension(128, 17));
        label_idade.setMinimumSize(new java.awt.Dimension(100, 17));
        label_idade.setPreferredSize(new java.awt.Dimension(100, 17));

        label_altura.setBackground(new java.awt.Color(255, 255, 255));
        label_altura.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_altura.setForeground(new java.awt.Color(255, 255, 255));
        label_altura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_altura.setMaximumSize(new java.awt.Dimension(128, 17));
        label_altura.setMinimumSize(new java.awt.Dimension(128, 17));
        label_altura.setPreferredSize(new java.awt.Dimension(128, 17));

        javax.swing.GroupLayout panel_labels_headLayout = new javax.swing.GroupLayout(panel_labels_head);
        panel_labels_head.setLayout(panel_labels_headLayout);
        panel_labels_headLayout.setHorizontalGroup(
            panel_labels_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label_idade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(label_altura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(label_nome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panel_labels_headLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(label_foto, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_labels_headLayout.setVerticalGroup(
            panel_labels_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_labels_headLayout.createSequentialGroup()
                .addComponent(label_foto, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_idade, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_altura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_btn_home.setBackground(new java.awt.Color(214, 214, 252));
        panel_btn_home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_btn_homeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel_btn_homeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel_btn_homeMouseExited(evt);
            }
        });

        label_home.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        label_home.setForeground(new java.awt.Color(255, 255, 255));
        label_home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/home32.png"))); // NOI18N
        label_home.setText("Inicio.");

        javax.swing.GroupLayout panel_btn_homeLayout = new javax.swing.GroupLayout(panel_btn_home);
        panel_btn_home.setLayout(panel_btn_homeLayout);
        panel_btn_homeLayout.setHorizontalGroup(
            panel_btn_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_btn_homeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_home, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_btn_homeLayout.setVerticalGroup(
            panel_btn_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label_home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panel_btn_conta.setBackground(new java.awt.Color(214, 214, 252));
        panel_btn_conta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_btn_contaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel_btn_contaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel_btn_contaMouseExited(evt);
            }
        });

        label_home1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        label_home1.setForeground(new java.awt.Color(255, 255, 255));
        label_home1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/user32.png"))); // NOI18N
        label_home1.setText("Conta.");

        javax.swing.GroupLayout panel_btn_contaLayout = new javax.swing.GroupLayout(panel_btn_conta);
        panel_btn_conta.setLayout(panel_btn_contaLayout);
        panel_btn_contaLayout.setHorizontalGroup(
            panel_btn_contaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_btn_contaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_home1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_btn_contaLayout.setVerticalGroup(
            panel_btn_contaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label_home1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel_headLayout = new javax.swing.GroupLayout(panel_head);
        panel_head.setLayout(panel_headLayout);
        panel_headLayout.setHorizontalGroup(
            panel_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_headLayout.createSequentialGroup()
                .addGroup(panel_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panel_btn_informativo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_btn_sair, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                    .addGroup(panel_headLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panel_labels_head, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panel_btn_conta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_btn_medicos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_btn_avaliacao_diaria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_btn_home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_headLayout.setVerticalGroup(
            panel_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_headLayout.createSequentialGroup()
                .addComponent(panel_labels_head, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panel_btn_home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_btn_conta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_btn_medicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_btn_avaliacao_diaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_btn_informativo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_btn_sair, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panel_desk.setBackground(new java.awt.Color(168, 168, 245));
        panel_desk.setMaximumSize(new java.awt.Dimension(494, 500));
        panel_desk.setMinimumSize(new java.awt.Dimension(494, 500));
        panel_desk.setPreferredSize(new java.awt.Dimension(494, 500));
        panel_desk.setLayout(new java.awt.CardLayout());

        panel_desk_main.setBackground(new java.awt.Color(255, 255, 255));
        panel_desk_main.setMaximumSize(new java.awt.Dimension(496, 500));
        panel_desk_main.setMinimumSize(new java.awt.Dimension(496, 500));
        panel_desk_main.setPreferredSize(new java.awt.Dimension(496, 500));

        panel_desk_main_head.setBackground(new java.awt.Color(214, 214, 252));
        panel_desk_main_head.setForeground(new java.awt.Color(0, 0, 51));
        panel_desk_main_head.setMaximumSize(new java.awt.Dimension(496, 100));
        panel_desk_main_head.setMinimumSize(new java.awt.Dimension(496, 100));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/dadosRecentes32.png"))); // NOI18N
        jLabel9.setText("Dados Mais Recentes.");

        jLabel10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("PS:");

        label_ps_recente.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_ps_recente.setForeground(new java.awt.Color(255, 255, 255));
        label_ps_recente.setMaximumSize(new java.awt.Dimension(30, 18));
        label_ps_recente.setMinimumSize(new java.awt.Dimension(30, 18));
        label_ps_recente.setPreferredSize(new java.awt.Dimension(30, 18));

        label_pd_recente.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_pd_recente.setForeground(new java.awt.Color(255, 255, 255));
        label_pd_recente.setMaximumSize(new java.awt.Dimension(30, 18));
        label_pd_recente.setMinimumSize(new java.awt.Dimension(30, 18));
        label_pd_recente.setPreferredSize(new java.awt.Dimension(30, 18));

        jLabel11.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("PD:");

        label_imc_recente.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_imc_recente.setForeground(new java.awt.Color(255, 255, 255));
        label_imc_recente.setText("  ");
        label_imc_recente.setMaximumSize(new java.awt.Dimension(30, 18));
        label_imc_recente.setMinimumSize(new java.awt.Dimension(30, 18));
        label_imc_recente.setPreferredSize(new java.awt.Dimension(30, 18));

        jLabel12.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("IMC");

        jLabel19.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Peso:");

        label_peso_recente.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_peso_recente.setForeground(new java.awt.Color(255, 255, 255));
        label_peso_recente.setMaximumSize(new java.awt.Dimension(30, 18));
        label_peso_recente.setMinimumSize(new java.awt.Dimension(30, 18));
        label_peso_recente.setPreferredSize(new java.awt.Dimension(30, 18));

        label555.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        label555.setForeground(new java.awt.Color(255, 255, 255));
        label555.setText("Classificação/Pressão:");

        label_class_pressao_recente.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_class_pressao_recente.setForeground(new java.awt.Color(255, 255, 255));
        label_class_pressao_recente.setMaximumSize(new java.awt.Dimension(100, 18));
        label_class_pressao_recente.setMinimumSize(new java.awt.Dimension(60, 18));
        label_class_pressao_recente.setPreferredSize(new java.awt.Dimension(100, 18));

        label_class_imc_recente.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_class_imc_recente.setForeground(new java.awt.Color(255, 255, 255));
        label_class_imc_recente.setMaximumSize(new java.awt.Dimension(100, 18));
        label_class_imc_recente.setMinimumSize(new java.awt.Dimension(60, 18));
        label_class_imc_recente.setPreferredSize(new java.awt.Dimension(100, 18));

        label_classification_imc.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        label_classification_imc.setForeground(new java.awt.Color(255, 255, 255));
        label_classification_imc.setText("Classificação/IMC:");

        javax.swing.GroupLayout panel_desk_main_headLayout = new javax.swing.GroupLayout(panel_desk_main_head);
        panel_desk_main_head.setLayout(panel_desk_main_headLayout);
        panel_desk_main_headLayout.setHorizontalGroup(
            panel_desk_main_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_desk_main_headLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_desk_main_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_desk_main_headLayout.createSequentialGroup()
                        .addGroup(panel_desk_main_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_desk_main_headLayout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_peso_recente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(label_imc_recente, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(label_classification_imc))
                            .addGroup(panel_desk_main_headLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_ps_recente, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(label_pd_recente, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label555)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(panel_desk_main_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(label_class_imc_recente, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                            .addComponent(label_class_pressao_recente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10))
                    .addGroup(panel_desk_main_headLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panel_desk_main_headLayout.setVerticalGroup(
            panel_desk_main_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_desk_main_headLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_desk_main_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_desk_main_headLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_desk_main_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_pd_recente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addGroup(panel_desk_main_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(label_ps_recente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(label555))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_desk_main_headLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(label_peso_recente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(label_imc_recente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_classification_imc)))
                    .addGroup(panel_desk_main_headLayout.createSequentialGroup()
                        .addComponent(label_class_pressao_recente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label_class_imc_recente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        panel_desk_main_desk.setBackground(new java.awt.Color(255, 255, 255));
        panel_desk_main_desk.setMaximumSize(new java.awt.Dimension(496, 390));
        panel_desk_main_desk.setMinimumSize(new java.awt.Dimension(496, 390));
        panel_desk_main_desk.setPreferredSize(new java.awt.Dimension(496, 390));

        panel_btn_progressao_peso_imc.setBackground(new java.awt.Color(255, 255, 255));
        panel_btn_progressao_peso_imc.setMaximumSize(new java.awt.Dimension(200, 170));
        panel_btn_progressao_peso_imc.setMinimumSize(new java.awt.Dimension(200, 170));
        panel_btn_progressao_peso_imc.setPreferredSize(new java.awt.Dimension(200, 170));

        label_title_chart_peso_imc.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        label_title_chart_peso_imc.setForeground(new java.awt.Color(0, 0, 51));
        label_title_chart_peso_imc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/peso32.png"))); // NOI18N
        label_title_chart_peso_imc.setText("Progressão Peso X IMC");
        label_title_chart_peso_imc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_title_chart_peso_imcMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                label_title_chart_peso_imcMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                label_title_chart_peso_imcMouseExited(evt);
            }
        });

        panel_grafico_peso_imc.setBackground(new java.awt.Color(255, 255, 255));
        panel_grafico_peso_imc.setForeground(new java.awt.Color(0, 0, 51));

        javax.swing.GroupLayout panel_grafico_peso_imcLayout = new javax.swing.GroupLayout(panel_grafico_peso_imc);
        panel_grafico_peso_imc.setLayout(panel_grafico_peso_imcLayout);
        panel_grafico_peso_imcLayout.setHorizontalGroup(
            panel_grafico_peso_imcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panel_grafico_peso_imcLayout.setVerticalGroup(
            panel_grafico_peso_imcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 136, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel_btn_progressao_peso_imcLayout = new javax.swing.GroupLayout(panel_btn_progressao_peso_imc);
        panel_btn_progressao_peso_imc.setLayout(panel_btn_progressao_peso_imcLayout);
        panel_btn_progressao_peso_imcLayout.setHorizontalGroup(
            panel_btn_progressao_peso_imcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_btn_progressao_peso_imcLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_btn_progressao_peso_imcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_grafico_peso_imc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_btn_progressao_peso_imcLayout.createSequentialGroup()
                        .addComponent(label_title_chart_peso_imc)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_btn_progressao_peso_imcLayout.setVerticalGroup(
            panel_btn_progressao_peso_imcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_btn_progressao_peso_imcLayout.createSequentialGroup()
                .addComponent(label_title_chart_peso_imc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_grafico_peso_imc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel_btn_progressao_ps_pd.setBackground(new java.awt.Color(255, 255, 255));
        panel_btn_progressao_ps_pd.setMaximumSize(new java.awt.Dimension(200, 170));
        panel_btn_progressao_ps_pd.setMinimumSize(new java.awt.Dimension(200, 170));
        panel_btn_progressao_ps_pd.setPreferredSize(new java.awt.Dimension(200, 170));

        label_title_chart_ps_pd.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        label_title_chart_ps_pd.setForeground(new java.awt.Color(0, 0, 51));
        label_title_chart_ps_pd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/pressure32.png"))); // NOI18N
        label_title_chart_ps_pd.setText("Progressão PS X PD");
        label_title_chart_ps_pd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_title_chart_ps_pdMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                label_title_chart_ps_pdMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                label_title_chart_ps_pdMouseExited(evt);
            }
        });

        panel_grafico_ps_pd.setBackground(new java.awt.Color(255, 255, 255));
        panel_grafico_ps_pd.setForeground(new java.awt.Color(0, 0, 51));

        javax.swing.GroupLayout panel_grafico_ps_pdLayout = new javax.swing.GroupLayout(panel_grafico_ps_pd);
        panel_grafico_ps_pd.setLayout(panel_grafico_ps_pdLayout);
        panel_grafico_ps_pdLayout.setHorizontalGroup(
            panel_grafico_ps_pdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panel_grafico_ps_pdLayout.setVerticalGroup(
            panel_grafico_ps_pdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 136, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel_btn_progressao_ps_pdLayout = new javax.swing.GroupLayout(panel_btn_progressao_ps_pd);
        panel_btn_progressao_ps_pd.setLayout(panel_btn_progressao_ps_pdLayout);
        panel_btn_progressao_ps_pdLayout.setHorizontalGroup(
            panel_btn_progressao_ps_pdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_btn_progressao_ps_pdLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_btn_progressao_ps_pdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_grafico_ps_pd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_btn_progressao_ps_pdLayout.createSequentialGroup()
                        .addComponent(label_title_chart_ps_pd)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_btn_progressao_ps_pdLayout.setVerticalGroup(
            panel_btn_progressao_ps_pdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_btn_progressao_ps_pdLayout.createSequentialGroup()
                .addComponent(label_title_chart_ps_pd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_grafico_ps_pd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_desk_main_deskLayout = new javax.swing.GroupLayout(panel_desk_main_desk);
        panel_desk_main_desk.setLayout(panel_desk_main_deskLayout);
        panel_desk_main_deskLayout.setHorizontalGroup(
            panel_desk_main_deskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_btn_progressao_ps_pd, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
            .addComponent(panel_btn_progressao_peso_imc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
        );
        panel_desk_main_deskLayout.setVerticalGroup(
            panel_desk_main_deskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_desk_main_deskLayout.createSequentialGroup()
                .addComponent(panel_btn_progressao_peso_imc, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_btn_progressao_ps_pd, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panel_desk_mainLayout = new javax.swing.GroupLayout(panel_desk_main);
        panel_desk_main.setLayout(panel_desk_mainLayout);
        panel_desk_mainLayout.setHorizontalGroup(
            panel_desk_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_desk_main_desk, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
            .addComponent(panel_desk_main_head, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_desk_mainLayout.setVerticalGroup(
            panel_desk_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_desk_mainLayout.createSequentialGroup()
                .addComponent(panel_desk_main_head, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel_desk_main_desk, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_desk.add(panel_desk_main, "card5");

        panel_progressao_peso_imc.setBackground(new java.awt.Color(255, 255, 255));
        panel_progressao_peso_imc.setMaximumSize(new java.awt.Dimension(496, 500));
        panel_progressao_peso_imc.setMinimumSize(new java.awt.Dimension(496, 500));

        panel_progressao_peso_imc_conteiner_chart.setBackground(new java.awt.Color(255, 255, 255));

        jLabel26.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 51));
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/peso32.png"))); // NOI18N
        jLabel26.setText("Progressão Peso X IMC");

        jLabel27.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 0, 51));
        jLabel27.setText("Data Inicio:");

        jLabel28.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 51));
        jLabel28.setText("Data Fim:");

        f_data_inicio_grafico_peso_imc.setForeground(new java.awt.Color(0, 0, 51));
        try {
            f_data_inicio_grafico_peso_imc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        f_data_inicio_grafico_peso_imc.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        f_data_fim_grafico_peso_imc.setForeground(new java.awt.Color(0, 0, 51));
        try {
            f_data_fim_grafico_peso_imc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        f_data_fim_grafico_peso_imc.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        panel_grafico_peso_imc_datado.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panel_grafico_peso_imc_datadoLayout = new javax.swing.GroupLayout(panel_grafico_peso_imc_datado);
        panel_grafico_peso_imc_datado.setLayout(panel_grafico_peso_imc_datadoLayout);
        panel_grafico_peso_imc_datadoLayout.setHorizontalGroup(
            panel_grafico_peso_imc_datadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panel_grafico_peso_imc_datadoLayout.setVerticalGroup(
            panel_grafico_peso_imc_datadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );

        btn_gerar_grafico_peso_imc.setForeground(new java.awt.Color(0, 0, 51));
        btn_gerar_grafico_peso_imc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/correct24.png"))); // NOI18N
        btn_gerar_grafico_peso_imc.setText("Gerar");
        btn_gerar_grafico_peso_imc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_gerar_grafico_peso_imcActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_progressao_peso_imc_conteiner_chartLayout = new javax.swing.GroupLayout(panel_progressao_peso_imc_conteiner_chart);
        panel_progressao_peso_imc_conteiner_chart.setLayout(panel_progressao_peso_imc_conteiner_chartLayout);
        panel_progressao_peso_imc_conteiner_chartLayout.setHorizontalGroup(
            panel_progressao_peso_imc_conteiner_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_progressao_peso_imc_conteiner_chartLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_gerar_grafico_peso_imc)
                .addContainerGap())
            .addGroup(panel_progressao_peso_imc_conteiner_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panel_progressao_peso_imc_conteiner_chartLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panel_progressao_peso_imc_conteiner_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel_grafico_peso_imc_datado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel_progressao_peso_imc_conteiner_chartLayout.createSequentialGroup()
                            .addGroup(panel_progressao_peso_imc_conteiner_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel26)
                                .addGroup(panel_progressao_peso_imc_conteiner_chartLayout.createSequentialGroup()
                                    .addComponent(jLabel27)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(f_data_inicio_grafico_peso_imc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(8, 8, 8)
                                    .addComponent(jLabel28)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(f_data_fim_grafico_peso_imc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 133, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        panel_progressao_peso_imc_conteiner_chartLayout.setVerticalGroup(
            panel_progressao_peso_imc_conteiner_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_progressao_peso_imc_conteiner_chartLayout.createSequentialGroup()
                .addContainerGap(174, Short.MAX_VALUE)
                .addComponent(btn_gerar_grafico_peso_imc)
                .addContainerGap())
            .addGroup(panel_progressao_peso_imc_conteiner_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panel_progressao_peso_imc_conteiner_chartLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel26)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(panel_grafico_peso_imc_datado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(panel_progressao_peso_imc_conteiner_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel27)
                        .addComponent(jLabel28)
                        .addComponent(f_data_inicio_grafico_peso_imc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(f_data_fim_grafico_peso_imc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        panel_calendar_data.setBackground(new java.awt.Color(255, 255, 255));

        f_imc_calendar.setEditable(false);
        f_imc_calendar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        f_imc_calendar.setForeground(new java.awt.Color(0, 0, 51));

        jLabel32.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 51));
        jLabel32.setText("Peso:");

        f_data_calendar_peso_imc.setEditable(false);
        f_data_calendar_peso_imc.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        f_data_calendar_peso_imc.setForeground(new java.awt.Color(0, 0, 51));

        jLabel31.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 0, 51));
        jLabel31.setText("IMC:");

        btn_dica_do_dia_peso_imc.setForeground(new java.awt.Color(0, 0, 51));
        btn_dica_do_dia_peso_imc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clue24.png"))); // NOI18N
        btn_dica_do_dia_peso_imc.setText("Dica do Dia");
        btn_dica_do_dia_peso_imc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dica_do_dia_peso_imcActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 0, 51));
        jLabel30.setText("Data :");

        f_peso_calendar.setEditable(false);
        f_peso_calendar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        f_peso_calendar.setForeground(new java.awt.Color(0, 0, 51));

        calendar_peso_imc.setBackground(new java.awt.Color(255, 255, 255));
        calendar_peso_imc.setForeground(new java.awt.Color(0, 0, 51));
        calendar_peso_imc.setDate(new java.util.Date(1586794130000L));
        calendar_peso_imc.setDecorationBackgroundColor(new java.awt.Color(255, 255, 255));
        calendar_peso_imc.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        btn_gerar_dados_peso_imc.setForeground(new java.awt.Color(0, 0, 51));
        btn_gerar_dados_peso_imc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/correct24.png"))); // NOI18N
        btn_gerar_dados_peso_imc.setText("Gerar");
        btn_gerar_dados_peso_imc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_gerar_dados_peso_imcActionPerformed(evt);
            }
        });

        brn_limpar_panel_peso_imc.setForeground(new java.awt.Color(0, 0, 51));
        brn_limpar_panel_peso_imc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clean24.png"))); // NOI18N
        brn_limpar_panel_peso_imc.setText("Limpar");
        brn_limpar_panel_peso_imc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brn_limpar_panel_peso_imcActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_calendar_dataLayout = new javax.swing.GroupLayout(panel_calendar_data);
        panel_calendar_data.setLayout(panel_calendar_dataLayout);
        panel_calendar_dataLayout.setHorizontalGroup(
            panel_calendar_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_calendar_dataLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_calendar_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_calendar_dataLayout.createSequentialGroup()
                        .addComponent(f_data_calendar_peso_imc, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panel_calendar_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panel_calendar_dataLayout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(f_imc_calendar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_calendar_dataLayout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(f_peso_calendar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 166, Short.MAX_VALUE))
                    .addGroup(panel_calendar_dataLayout.createSequentialGroup()
                        .addComponent(btn_dica_do_dia_peso_imc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(brn_limpar_panel_peso_imc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_gerar_dados_peso_imc)))
                .addContainerGap())
            .addGroup(panel_calendar_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panel_calendar_dataLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(calendar_peso_imc, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        panel_calendar_dataLayout.setVerticalGroup(
            panel_calendar_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_calendar_dataLayout.createSequentialGroup()
                .addContainerGap(201, Short.MAX_VALUE)
                .addGroup(panel_calendar_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(f_data_calendar_peso_imc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(f_imc_calendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_calendar_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(f_peso_calendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel_calendar_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(brn_limpar_panel_peso_imc)
                    .addComponent(btn_gerar_dados_peso_imc)
                    .addComponent(btn_dica_do_dia_peso_imc))
                .addContainerGap())
            .addGroup(panel_calendar_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panel_calendar_dataLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(calendar_peso_imc, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(159, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout panel_progressao_peso_imcLayout = new javax.swing.GroupLayout(panel_progressao_peso_imc);
        panel_progressao_peso_imc.setLayout(panel_progressao_peso_imcLayout);
        panel_progressao_peso_imcLayout.setHorizontalGroup(
            panel_progressao_peso_imcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_progressao_peso_imcLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panel_progressao_peso_imcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_progressao_peso_imc_conteiner_chart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_calendar_data, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        panel_progressao_peso_imcLayout.setVerticalGroup(
            panel_progressao_peso_imcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_progressao_peso_imcLayout.createSequentialGroup()
                .addComponent(panel_progressao_peso_imc_conteiner_chart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(panel_calendar_data, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        panel_desk.add(panel_progressao_peso_imc, "card5");

        panel_progressao_ps_pd.setBackground(new java.awt.Color(255, 255, 255));
        panel_progressao_ps_pd.setMaximumSize(new java.awt.Dimension(496, 500));
        panel_progressao_ps_pd.setMinimumSize(new java.awt.Dimension(496, 500));
        panel_progressao_ps_pd.setName(""); // NOI18N
        panel_progressao_ps_pd.setPreferredSize(new java.awt.Dimension(496, 500));

        panel_progressao_ps_pd_conteiner_chart.setBackground(new java.awt.Color(255, 255, 255));

        jLabel44.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(0, 0, 51));
        jLabel44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/pressure32.png"))); // NOI18N
        jLabel44.setText("Progressão PS X PD");

        panel_grafico_ps_pd_datado.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panel_grafico_ps_pd_datadoLayout = new javax.swing.GroupLayout(panel_grafico_ps_pd_datado);
        panel_grafico_ps_pd_datado.setLayout(panel_grafico_ps_pd_datadoLayout);
        panel_grafico_ps_pd_datadoLayout.setHorizontalGroup(
            panel_grafico_ps_pd_datadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panel_grafico_ps_pd_datadoLayout.setVerticalGroup(
            panel_grafico_ps_pd_datadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );

        f_data_inicio_grafico_ps_pd.setForeground(new java.awt.Color(0, 0, 51));
        try {
            f_data_inicio_grafico_ps_pd.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        f_data_inicio_grafico_ps_pd.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        f_data_fim_grafico_ps_pd.setForeground(new java.awt.Color(0, 0, 51));
        try {
            f_data_fim_grafico_ps_pd.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        f_data_fim_grafico_ps_pd.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        btn_gerar_grafico_ps_pd.setForeground(new java.awt.Color(0, 0, 51));
        btn_gerar_grafico_ps_pd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/correct24.png"))); // NOI18N
        btn_gerar_grafico_ps_pd.setText("Gerar");
        btn_gerar_grafico_ps_pd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_gerar_grafico_ps_pdMouseClicked(evt);
            }
        });
        btn_gerar_grafico_ps_pd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_gerar_grafico_ps_pdActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(0, 0, 51));
        jLabel39.setText("Data Inicio:");

        jLabel41.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 0, 51));
        jLabel41.setText("Data Fim:");

        javax.swing.GroupLayout panel_progressao_ps_pd_conteiner_chartLayout = new javax.swing.GroupLayout(panel_progressao_ps_pd_conteiner_chart);
        panel_progressao_ps_pd_conteiner_chart.setLayout(panel_progressao_ps_pd_conteiner_chartLayout);
        panel_progressao_ps_pd_conteiner_chartLayout.setHorizontalGroup(
            panel_progressao_ps_pd_conteiner_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_progressao_ps_pd_conteiner_chartLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(f_data_inicio_grafico_ps_pd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(f_data_fim_grafico_ps_pd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_gerar_grafico_ps_pd)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panel_progressao_ps_pd_conteiner_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panel_progressao_ps_pd_conteiner_chartLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panel_progressao_ps_pd_conteiner_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel_grafico_ps_pd_datado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel_progressao_ps_pd_conteiner_chartLayout.createSequentialGroup()
                            .addComponent(jLabel44)
                            .addGap(0, 246, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        panel_progressao_ps_pd_conteiner_chartLayout.setVerticalGroup(
            panel_progressao_ps_pd_conteiner_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_progressao_ps_pd_conteiner_chartLayout.createSequentialGroup()
                .addContainerGap(174, Short.MAX_VALUE)
                .addGroup(panel_progressao_ps_pd_conteiner_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(jLabel41)
                    .addComponent(f_data_inicio_grafico_ps_pd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(f_data_fim_grafico_ps_pd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_gerar_grafico_ps_pd))
                .addGap(12, 12, 12))
            .addGroup(panel_progressao_ps_pd_conteiner_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panel_progressao_ps_pd_conteiner_chartLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel44)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(panel_grafico_ps_pd_datado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(50, Short.MAX_VALUE)))
        );

        panel_calendar_ps_pd_data.setBackground(new java.awt.Color(255, 255, 255));

        calendar_ps_pd.setForeground(new java.awt.Color(0, 0, 51));
        calendar_ps_pd.setDecorationBackgroundColor(new java.awt.Color(255, 255, 255));
        calendar_ps_pd.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        f_data_calendar_ps_pd.setEditable(false);
        f_data_calendar_ps_pd.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        f_data_calendar_ps_pd.setForeground(new java.awt.Color(0, 0, 51));

        jLabel38.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 0, 51));
        jLabel38.setText("Pressão Sistolica:");

        f_ps_calendar.setEditable(false);
        f_ps_calendar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        f_ps_calendar.setForeground(new java.awt.Color(0, 0, 51));

        jLabel40.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 0, 51));
        jLabel40.setText("Pressão Diastólica");

        f_pd_calendar.setEditable(false);
        f_pd_calendar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        f_pd_calendar.setForeground(new java.awt.Color(0, 0, 51));

        jLabel45.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 0, 51));
        jLabel45.setText("Data :");

        btn_gerar_dados_ps_pd.setForeground(new java.awt.Color(0, 0, 51));
        btn_gerar_dados_ps_pd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/correct24.png"))); // NOI18N
        btn_gerar_dados_ps_pd.setText("Gerar");
        btn_gerar_dados_ps_pd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_gerar_dados_ps_pdActionPerformed(evt);
            }
        });

        btn_dica_do_dia_ps_pd.setForeground(new java.awt.Color(0, 0, 51));
        btn_dica_do_dia_ps_pd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clue24.png"))); // NOI18N
        btn_dica_do_dia_ps_pd.setText("Dica do Dia");
        btn_dica_do_dia_ps_pd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dica_do_dia_ps_pdActionPerformed(evt);
            }
        });

        brn_limpar_progressao_ps_pd.setForeground(new java.awt.Color(0, 0, 51));
        brn_limpar_progressao_ps_pd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clean24.png"))); // NOI18N
        brn_limpar_progressao_ps_pd.setText("Limpar");
        brn_limpar_progressao_ps_pd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brn_limpar_progressao_ps_pdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_calendar_ps_pd_dataLayout = new javax.swing.GroupLayout(panel_calendar_ps_pd_data);
        panel_calendar_ps_pd_data.setLayout(panel_calendar_ps_pd_dataLayout);
        panel_calendar_ps_pd_dataLayout.setHorizontalGroup(
            panel_calendar_ps_pd_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_calendar_ps_pd_dataLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_calendar_ps_pd_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_calendar_ps_pd_dataLayout.createSequentialGroup()
                        .addComponent(btn_dica_do_dia_ps_pd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(brn_limpar_progressao_ps_pd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_gerar_dados_ps_pd))
                    .addGroup(panel_calendar_ps_pd_dataLayout.createSequentialGroup()
                        .addGroup(panel_calendar_ps_pd_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panel_calendar_ps_pd_dataLayout.createSequentialGroup()
                                .addComponent(f_data_calendar_ps_pd, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(jLabel38))
                            .addComponent(jLabel40))
                        .addGap(18, 18, 18)
                        .addGroup(panel_calendar_ps_pd_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(f_ps_calendar)
                            .addComponent(f_pd_calendar, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 83, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(panel_calendar_ps_pd_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panel_calendar_ps_pd_dataLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(calendar_ps_pd, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        panel_calendar_ps_pd_dataLayout.setVerticalGroup(
            panel_calendar_ps_pd_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_calendar_ps_pd_dataLayout.createSequentialGroup()
                .addGap(176, 176, 176)
                .addGroup(panel_calendar_ps_pd_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_calendar_ps_pd_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel45)
                        .addComponent(f_data_calendar_ps_pd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel38)
                        .addComponent(f_ps_calendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_calendar_ps_pd_dataLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(panel_calendar_ps_pd_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(f_pd_calendar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel40))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel_calendar_ps_pd_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_gerar_dados_ps_pd)
                    .addComponent(brn_limpar_progressao_ps_pd)
                    .addComponent(btn_dica_do_dia_ps_pd))
                .addContainerGap())
            .addGroup(panel_calendar_ps_pd_dataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panel_calendar_ps_pd_dataLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(calendar_ps_pd, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(136, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout panel_progressao_ps_pdLayout = new javax.swing.GroupLayout(panel_progressao_ps_pd);
        panel_progressao_ps_pd.setLayout(panel_progressao_ps_pdLayout);
        panel_progressao_ps_pdLayout.setHorizontalGroup(
            panel_progressao_ps_pdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_progressao_ps_pdLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panel_progressao_ps_pdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_progressao_ps_pd_conteiner_chart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_calendar_ps_pd_data, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        panel_progressao_ps_pdLayout.setVerticalGroup(
            panel_progressao_ps_pdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_progressao_ps_pdLayout.createSequentialGroup()
                .addComponent(panel_progressao_ps_pd_conteiner_chart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_calendar_ps_pd_data, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_desk.add(panel_progressao_ps_pd, "card6");

        panel_registrar_medico.setBackground(new java.awt.Color(255, 255, 255));
        panel_registrar_medico.setMaximumSize(new java.awt.Dimension(496, 300));
        panel_registrar_medico.setMinimumSize(new java.awt.Dimension(496, 300));
        panel_registrar_medico.setPreferredSize(new java.awt.Dimension(496, 300));

        label_registro_medico.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        label_registro_medico.setForeground(new java.awt.Color(0, 0, 51));
        label_registro_medico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/doctor32.png"))); // NOI18N
        label_registro_medico.setText("Registro de Médico.");

        jLabel16.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 0, 0));
        jLabel16.setText("HealthSafe");

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/heartRed64.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText("CRA:");

        f_CRA.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setText("Nome:");

        f_nome_medico.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText("Email:");

        f_email_medico.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setText("Senha:");

        btn_registrar_medico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/correct24.png"))); // NOI18N
        btn_registrar_medico.setText("Registrar");
        btn_registrar_medico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registrar_medicoActionPerformed(evt);
            }
        });

        btn_voltar_medico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/voltar24.png"))); // NOI18N
        btn_voltar_medico.setText("Voltar");
        btn_voltar_medico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_voltar_medicoActionPerformed(evt);
            }
        });

        f_senha_medico.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        javax.swing.GroupLayout panel_registrar_medicoLayout = new javax.swing.GroupLayout(panel_registrar_medico);
        panel_registrar_medico.setLayout(panel_registrar_medicoLayout);
        panel_registrar_medicoLayout.setHorizontalGroup(
            panel_registrar_medicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_registrar_medicoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_registrar_medicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_registrar_medicoLayout.createSequentialGroup()
                        .addComponent(label_registro_medico)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 189, Short.MAX_VALUE)
                        .addGroup(panel_registrar_medicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)))
                    .addGroup(panel_registrar_medicoLayout.createSequentialGroup()
                        .addGroup(panel_registrar_medicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panel_registrar_medicoLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(f_nome_medico, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_registrar_medicoLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(44, 44, 44)
                                .addComponent(f_CRA, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_registrar_medicoLayout.createSequentialGroup()
                                .addGroup(panel_registrar_medicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panel_registrar_medicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(f_email_medico, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(f_senha_medico))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_registrar_medicoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_voltar_medico)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_registrar_medico)))
                .addContainerGap())
        );
        panel_registrar_medicoLayout.setVerticalGroup(
            panel_registrar_medicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_registrar_medicoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_registrar_medicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_registrar_medicoLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel16))
                    .addComponent(label_registro_medico))
                .addGap(12, 12, 12)
                .addGroup(panel_registrar_medicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(f_CRA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_registrar_medicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(f_nome_medico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_registrar_medicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(f_email_medico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_registrar_medicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(f_senha_medico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(panel_registrar_medicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_voltar_medico)
                    .addComponent(btn_registrar_medico))
                .addContainerGap(237, Short.MAX_VALUE))
        );

        panel_desk.add(panel_registrar_medico, "card2");

        panel_avaliacao_diaria.setBackground(new java.awt.Color(255, 255, 255));
        panel_avaliacao_diaria.setMaximumSize(new java.awt.Dimension(496, 500));
        panel_avaliacao_diaria.setMinimumSize(new java.awt.Dimension(496, 500));
        panel_avaliacao_diaria.setPreferredSize(new java.awt.Dimension(496, 500));

        label_registro_medico1.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        label_registro_medico1.setForeground(new java.awt.Color(0, 0, 51));
        label_registro_medico1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/dadosRecentes32.png"))); // NOI18N
        label_registro_medico1.setText("Avaliação Diária.");

        label_pressao_s_aval.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_pressao_s_aval.setText("Pressão Sistólica:");

        f_systolic_pressurel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        f_diastolic_pressure.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        label_pressao_d_aval.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_pressao_d_aval.setText("Pressão Diastólica:");

        label_peso_aval.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_peso_aval.setText("Peso/Kg:");

        f_weight.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        label_peso_aval1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_peso_aval1.setText("Fumou ?");

        label_peso_aval2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_peso_aval2.setText("Bebeu ?");

        label_peso_aval3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_peso_aval3.setText("Exercitou-se ?");

        label_peso_aval4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_peso_aval4.setText("Comeu Fast-Food ?");

        label_peso_aval5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label_peso_aval5.setText("Senha:");

        box_smoke.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sim", "Não" }));

        box_alcool.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sim", "Não" }));

        box_exercise.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sim", "Não" }));

        box_fastfood.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sim", "Não" }));

        btn_registrar_aval.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/correct24.png"))); // NOI18N
        btn_registrar_aval.setText("Registrar");
        btn_registrar_aval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registrar_avalActionPerformed(evt);
            }
        });

        btn_voltar_aval.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cancel24.png"))); // NOI18N
        btn_voltar_aval.setText("Cancelar");
        btn_voltar_aval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_voltar_avalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_avaliacao_diariaLayout = new javax.swing.GroupLayout(panel_avaliacao_diaria);
        panel_avaliacao_diaria.setLayout(panel_avaliacao_diariaLayout);
        panel_avaliacao_diariaLayout.setHorizontalGroup(
            panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_avaliacao_diariaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_registro_medico1)
                    .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_avaliacao_diariaLayout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_voltar_aval)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btn_registrar_aval))
                        .addGroup(panel_avaliacao_diariaLayout.createSequentialGroup()
                            .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(label_peso_aval2)
                                .addComponent(label_peso_aval4)
                                .addComponent(label_peso_aval3)
                                .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_avaliacao_diariaLayout.createSequentialGroup()
                                        .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(label_peso_aval, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label_peso_aval1))
                                        .addGap(83, 83, 83)
                                        .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(box_smoke, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(box_alcool, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(box_exercise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(box_fastfood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(f_senha_aval, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(f_weight, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_avaliacao_diariaLayout.createSequentialGroup()
                                            .addComponent(label_pressao_d_aval)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(f_diastolic_pressure, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_avaliacao_diariaLayout.createSequentialGroup()
                                            .addComponent(label_pressao_s_aval)
                                            .addGap(44, 44, 44)
                                            .addComponent(f_systolic_pressurel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addComponent(label_peso_aval5))
                            .addGap(128, 128, 128))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_avaliacao_diariaLayout.setVerticalGroup(
            panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_avaliacao_diariaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_registro_medico1)
                .addGap(65, 65, 65)
                .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_pressao_s_aval)
                    .addComponent(f_systolic_pressurel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_pressao_d_aval)
                    .addComponent(f_diastolic_pressure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_peso_aval)
                    .addComponent(f_weight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_peso_aval1)
                    .addComponent(box_smoke, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_peso_aval2)
                    .addComponent(box_alcool, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_peso_aval3)
                    .addComponent(box_exercise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_peso_aval4)
                    .addComponent(box_fastfood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_peso_aval5)
                    .addComponent(f_senha_aval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(panel_avaliacao_diariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_voltar_aval)
                    .addComponent(btn_registrar_aval))
                .addContainerGap(130, Short.MAX_VALUE))
        );

        panel_desk.add(panel_avaliacao_diaria, "card3");

        panel_medicos.setBackground(new java.awt.Color(255, 255, 255));
        panel_medicos.setMaximumSize(new java.awt.Dimension(496, 300));
        panel_medicos.setMinimumSize(new java.awt.Dimension(496, 300));

        tb_medicos.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tb_medicos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "CRA", "Email"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_medicos.setGridColor(new java.awt.Color(204, 204, 255));
        jScrollPane1.setViewportView(tb_medicos);
        if (tb_medicos.getColumnModel().getColumnCount() > 0) {
            tb_medicos.getColumnModel().getColumn(0).setResizable(false);
            tb_medicos.getColumnModel().getColumn(1).setResizable(false);
            tb_medicos.getColumnModel().getColumn(2).setResizable(false);
        }

        btn_cadastrar_medico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/plus24.png"))); // NOI18N
        btn_cadastrar_medico.setText("Cadastrar Médico");
        btn_cadastrar_medico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cadastrar_medicoActionPerformed(evt);
            }
        });

        btn_excluir_medico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete24.png"))); // NOI18N
        btn_excluir_medico.setText("Excluir");
        btn_excluir_medico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_excluir_medicoActionPerformed(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(0, 0, 51));
        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/doctor32.png"))); // NOI18N
        jLabel46.setText("Médicos.");

        javax.swing.GroupLayout panel_medicosLayout = new javax.swing.GroupLayout(panel_medicos);
        panel_medicos.setLayout(panel_medicosLayout);
        panel_medicosLayout.setHorizontalGroup(
            panel_medicosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_medicosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_medicosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_medicosLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_excluir_medico)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_cadastrar_medico))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                    .addGroup(panel_medicosLayout.createSequentialGroup()
                        .addComponent(jLabel46)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_medicosLayout.setVerticalGroup(
            panel_medicosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_medicosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_medicosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cadastrar_medico, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_excluir_medico, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(107, Short.MAX_VALUE))
        );

        panel_desk.add(panel_medicos, "card2");

        panel_perfil_usuario.setBackground(new java.awt.Color(255, 255, 255));
        panel_perfil_usuario.setMaximumSize(new java.awt.Dimension(496, 300));
        panel_perfil_usuario.setMinimumSize(new java.awt.Dimension(496, 300));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 51));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/user32.png"))); // NOI18N
        jLabel1.setText("Conta.");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 51));
        jLabel8.setText("Login:");

        jLabel13.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 51));
        jLabel13.setText("Senha:");

        jLabel14.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 51));
        jLabel14.setText("Nome:");

        jLabel17.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 51));
        jLabel17.setText("Email:");

        jLabel18.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 51));
        jLabel18.setText("Data Nascimento:");

        f_pdata.setEditable(false);
        f_pdata.setForeground(new java.awt.Color(0, 0, 51));
        try {
            f_pdata.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        f_pdata.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        f_pemail.setEditable(false);
        f_pemail.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        f_pemail.setForeground(new java.awt.Color(0, 0, 51));

        f_pnome.setEditable(false);
        f_pnome.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        f_pnome.setForeground(new java.awt.Color(0, 0, 51));

        f_psenha.setEditable(false);
        f_psenha.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        f_psenha.setForeground(new java.awt.Color(0, 0, 51));

        f_plogin.setEditable(false);
        f_plogin.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        f_plogin.setForeground(new java.awt.Color(0, 0, 51));

        box_fumos.setForeground(new java.awt.Color(0, 0, 51));
        box_fumos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Não Fumo", "1-2 Dias", "2-4 Dias", "4+ Dias" }));

        jLabel20.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 51));
        jLabel20.setText("Fumos/Semana:");

        jLabel21.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 51));
        jLabel21.setText("Alcool/Semana:");

        jLabel22.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 51));
        jLabel22.setText("Exercicios/Semana:");

        box_exercicios.setForeground(new java.awt.Color(0, 0, 51));
        box_exercicios.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sedentário", "1-2 Dias", "2-4 Dias", "4+ Dias" }));

        box_alcool1.setForeground(new java.awt.Color(0, 0, 51));
        box_alcool1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Não Bebo", "1-2 Dias", "2-4 Dias", "4+ Dias" }));

        jLabel23.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 51));
        jLabel23.setText("Altura/Cm:");

        f_paltura.setEditable(false);
        f_paltura.setForeground(new java.awt.Color(0, 0, 51));
        f_paltura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###"))));

        btn_palterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/edit24.png"))); // NOI18N
        btn_palterar.setText("Alterar");
        btn_palterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_palterarActionPerformed(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 0, 51));
        jLabel48.setText("Sexo:");

        box_psexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Feminino" }));
        box_psexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                box_psexoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_perfil_usuarioLayout = new javax.swing.GroupLayout(panel_perfil_usuario);
        panel_perfil_usuario.setLayout(panel_perfil_usuarioLayout);
        panel_perfil_usuarioLayout.setHorizontalGroup(
            panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_perfil_usuarioLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_perfil_usuarioLayout.createSequentialGroup()
                        .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23)
                            .addComponent(jLabel48))
                        .addGap(24, 24, 24)
                        .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(box_psexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(box_alcool1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(box_fumos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(box_exercicios, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(f_paltura, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(168, 168, 168))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_perfil_usuarioLayout.createSequentialGroup()
                        .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(panel_perfil_usuarioLayout.createSequentialGroup()
                                .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel18))
                                .addGap(34, 34, 34)
                                .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(f_pdata, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(f_pnome, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                        .addComponent(f_psenha)
                                        .addComponent(f_plogin))
                                    .addComponent(f_pemail, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_perfil_usuarioLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(btn_palterar))
                            .addGroup(panel_perfil_usuarioLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(l_pfoto, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(22, 22, 22))))
        );
        panel_perfil_usuarioLayout.setVerticalGroup(
            panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_perfil_usuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_perfil_usuarioLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(f_plogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(f_psenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(f_pnome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)))
                    .addComponent(l_pfoto, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(f_pemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(f_pdata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(box_fumos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(box_alcool1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(box_exercicios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(f_paltura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_perfil_usuarioLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btn_palterar))
                    .addGroup(panel_perfil_usuarioLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(panel_perfil_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel48)
                            .addComponent(box_psexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(118, Short.MAX_VALUE))
        );

        panel_desk.add(panel_perfil_usuario, "card2");

        panel_atualizar_usuario.setBackground(new java.awt.Color(255, 255, 255));
        panel_atualizar_usuario.setMaximumSize(new java.awt.Dimension(496, 300));
        panel_atualizar_usuario.setMinimumSize(new java.awt.Dimension(496, 300));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 51));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/user32.png"))); // NOI18N
        jLabel2.setText("Conta.");

        l_afoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                l_afotoMouseClicked(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 51));
        jLabel24.setText("Login:");

        jLabel25.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 51));
        jLabel25.setText("Senha Nova: ");

        jLabel29.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 0, 51));
        jLabel29.setText("Nome:");

        jLabel33.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 0, 51));
        jLabel33.setText("Email:");

        jLabel34.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 0, 51));
        jLabel34.setText("Data Nascimento:");

        f_adata.setForeground(new java.awt.Color(0, 0, 51));
        try {
            f_adata.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        f_adata.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        f_aemail.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        f_aemail.setForeground(new java.awt.Color(0, 0, 51));

        f_anome.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        f_anome.setForeground(new java.awt.Color(0, 0, 51));

        f_asenha_nova.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        f_asenha_nova.setForeground(new java.awt.Color(0, 0, 51));

        f_alogin.setEditable(false);
        f_alogin.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        f_alogin.setForeground(new java.awt.Color(0, 0, 51));

        box_afumos.setForeground(new java.awt.Color(0, 0, 51));
        box_afumos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Não Fumo", "1-2 Dias", "2-4 Dias", "4+ Dias" }));

        jLabel35.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 0, 51));
        jLabel35.setText("Fumos/Semana:");

        jLabel36.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 0, 51));
        jLabel36.setText("Alcool/Semana:");

        jLabel37.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 0, 51));
        jLabel37.setText("Exercicios/Semana:");

        box_aexercise.setForeground(new java.awt.Color(0, 0, 51));
        box_aexercise.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sedentário", "1-2 Dias", "2-4 Dias", "4+ Dias" }));

        box_aalcool.setForeground(new java.awt.Color(0, 0, 51));
        box_aalcool.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Não Bebo", "1-2 Dias", "2-4 Dias", "4+ Dias" }));

        jLabel42.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 0, 51));
        jLabel42.setText("Altura/Cm:");

        f_aaltura.setForeground(new java.awt.Color(0, 0, 51));
        f_aaltura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###"))));

        btn_aalterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/correct24.png"))); // NOI18N
        btn_aalterar.setText("Alterar");
        btn_aalterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_aalterarActionPerformed(evt);
            }
        });

        btn_alimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clean24.png"))); // NOI18N
        btn_alimpar.setText("Limpar");
        btn_alimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_alimparActionPerformed(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(0, 0, 51));
        jLabel43.setText("Senha: ");

        f_asenha.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        f_asenha.setForeground(new java.awt.Color(0, 0, 51));

        box_asexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Feminino" }));
        box_asexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                box_asexoActionPerformed(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel47.setText("Sexo:");

        javax.swing.GroupLayout panel_atualizar_usuarioLayout = new javax.swing.GroupLayout(panel_atualizar_usuario);
        panel_atualizar_usuario.setLayout(panel_atualizar_usuarioLayout);
        panel_atualizar_usuarioLayout.setHorizontalGroup(
            panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_atualizar_usuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_atualizar_usuarioLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_alimpar)
                        .addGap(18, 18, 18)
                        .addComponent(btn_aalterar))
                    .addGroup(panel_atualizar_usuarioLayout.createSequentialGroup()
                        .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(panel_atualizar_usuarioLayout.createSequentialGroup()
                                .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel35)
                                    .addComponent(jLabel36)
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel42))
                                .addGap(24, 24, 24)
                                .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(box_aalcool, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(box_afumos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(box_aexercise, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(f_aaltura, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panel_atualizar_usuarioLayout.createSequentialGroup()
                                .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel29)
                                    .addComponent(jLabel33)
                                    .addComponent(jLabel34))
                                .addGap(34, 34, 34)
                                .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(f_adata, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(f_anome, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                        .addComponent(f_asenha_nova)
                                        .addComponent(f_alogin))
                                    .addComponent(f_aemail, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panel_atualizar_usuarioLayout.createSequentialGroup()
                                .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel43)
                                    .addComponent(jLabel47))
                                .addGap(91, 91, 91)
                                .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(box_asexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(f_asenha, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(l_afoto, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(39, 39, 39))
        );
        panel_atualizar_usuarioLayout.setVerticalGroup(
            panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_atualizar_usuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_atualizar_usuarioLayout.createSequentialGroup()
                        .addComponent(l_afoto, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_atualizar_usuarioLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(f_alogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(f_asenha_nova, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(f_anome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29))
                        .addGap(4, 4, 4)
                        .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(f_aemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(f_adata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(box_afumos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(box_aalcool, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(box_aexercise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(f_aaltura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43)
                            .addComponent(f_asenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel47)
                            .addComponent(box_asexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)))
                .addGroup(panel_atualizar_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_aalterar)
                    .addComponent(btn_alimpar))
                .addGap(88, 88, 88))
        );

        panel_desk.add(panel_atualizar_usuario, "card2");

        javax.swing.GroupLayout panel_mainLayout = new javax.swing.GroupLayout(panel_main);
        panel_main.setLayout(panel_mainLayout);
        panel_mainLayout.setHorizontalGroup(
            panel_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_mainLayout.createSequentialGroup()
                .addComponent(panel_head, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panel_desk, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE))
        );
        panel_mainLayout.setVerticalGroup(
            panel_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_head, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_desk, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_main, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_main, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void panel_btn_sairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_sairMouseClicked
        JOptionPane.showMessageDialog(null,"Muito obrigado pelo acesso, volte sempre !!");
        Login login = new Login();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_panel_btn_sairMouseClicked

    private void btn_registrar_avalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registrar_avalActionPerformed
        if(Utilitarios.validarCampos(panel_avaliacao_diaria)){
            Day_atualizationDAO day_a = new Day_atualizationDAO();
            day_a.setDay_Atualization(new java.util.Date());
            day_a.setSystolic_pressure(Float.parseFloat(f_systolic_pressurel.getText()));
            day_a.setDiastolic_pressure(Float.parseFloat(f_diastolic_pressure.getText()));
            day_a.setWeight(Float.parseFloat(f_weight.getText()));
            day_a.setAlcool(Utilitarios.trueorfalse(String.valueOf(box_alcool.getSelectedItem())));
            day_a.setSmoke(Utilitarios.trueorfalse(String.valueOf(box_smoke.getSelectedItem())));
            day_a.setExercise(Utilitarios.trueorfalse(String.valueOf(box_exercise.getSelectedItem())));
            day_a.setFast_food(Utilitarios.trueorfalse(String.valueOf(box_fastfood.getSelectedItem())));
            day_a.setClassification_pressure(Day_atualization.classification_pressure(Float.parseFloat(f_systolic_pressurel.getText()), Float.parseFloat(f_diastolic_pressure.getText())));
            float imc = (float) (Float.parseFloat(f_weight.getText())/Math.pow((this.user.getHeight()/100),2));
            day_a.setIMC(imc);
            day_a.setClassification_imc(Day_atualization.classification_imc(imc));
            day_a.setUserLogin(this.user.getLogin());
            day_a.setDay_tip(Day_tip.get_day_tip(day_a));
            Day_atualizationDAO.doctorEmail(this.user,day_a);
            if(f_senha_aval.getText().equals(this.user.getPass())){
                day_a.insert_day_atualization(day_a);
                atualizarSistema();
                Utilitarios.limparField(panel_avaliacao_diaria);
                Utilitarios.atualizarPainel(panel_desk, panel_desk_main);
                this.day_tip = Day_tipDAO.get_day_tip(day_a.getDay_tip());
                this.dica_do_dia();
            }else{
                JOptionPane.showMessageDialog(null,"Senha incorreta");
            }
            
        }
    }//GEN-LAST:event_btn_registrar_avalActionPerformed

    private void panel_btn_avaliacao_diariaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_avaliacao_diariaMouseClicked
        Utilitarios.atualizarPainel(panel_desk, panel_avaliacao_diaria);
        Utilitarios.changeBackground_entered(panel_btn_avaliacao_diaria);
    }//GEN-LAST:event_panel_btn_avaliacao_diariaMouseClicked

    private void btn_voltar_avalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_voltar_avalActionPerformed
        Utilitarios.limparField(panel_avaliacao_diaria);
        Utilitarios.atualizarPainel(panel_desk, panel_desk_main);
    }//GEN-LAST:event_btn_voltar_avalActionPerformed

    private void panel_btn_medicosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_medicosMouseClicked
        Utilitarios.atualizarPainel(panel_desk, panel_medicos);
        Utilitarios.changeBackground_entered(panel_btn_medicos);     
        atualizarSistema();
    }//GEN-LAST:event_panel_btn_medicosMouseClicked

    private void btn_registrar_medicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registrar_medicoActionPerformed
        if(Utilitarios.validarCampos(panel_registrar_medico)){
            if(Utilitarios.validarEmail(f_email_medico.getText())){
                if(f_senha_medico.getText().equals(this.user.getPass())){
                    DoctorDAO doc = new DoctorDAO();
                    doc.setCRA(f_CRA.getText());
                    doc.setEmail(f_email_medico.getText());
                    doc.setNome(f_nome_medico.getText());
                    doc.insert_doctor(doc, this.user.getLogin());
                    Utilitarios.limparField(panel_registrar_medico);
                    Utilitarios.atualizarPainel(panel_desk, panel_medicos);
                    atualizarSistema();
                }else{
                    JOptionPane.showMessageDialog(null,"Senha incorreta");
                }
            }
        
        
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_registrar_medicoActionPerformed

    private void btn_voltar_medicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_voltar_medicoActionPerformed
        Utilitarios.limparField(panel_registrar_medico);
        Utilitarios.atualizarPainel(panel_desk, panel_medicos);
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_voltar_medicoActionPerformed

    private void panel_btn_informativoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_informativoMouseClicked
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("http://departamentos.cardiol.br/dha/consenso3/capitulo7.asp"));
            } catch (URISyntaxException ex) {
                Logger.getLogger(MainUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }//GEN-LAST:event_panel_btn_informativoMouseClicked

    private void btn_gerar_grafico_peso_imcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_gerar_grafico_peso_imcActionPerformed
        this.atualizar_graficos_progressao_imc_peso();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_gerar_grafico_peso_imcActionPerformed

    private void brn_limpar_panel_peso_imcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brn_limpar_panel_peso_imcActionPerformed
        Utilitarios.limparField(panel_progressao_peso_imc);
        // TODO add your handling code here:
    }//GEN-LAST:event_brn_limpar_panel_peso_imcActionPerformed

    private void btn_gerar_dados_peso_imcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_gerar_dados_peso_imcActionPerformed
        try {
            this.get_dados_day_calendar_peso_imc(new java.sql.Date(this.sdf.parse(this.sdf.format(calendar_peso_imc.getDate())).getTime()));
            // TODO add your handling code here:
        } catch (ParseException ex) {
            Logger.getLogger(MainUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_gerar_dados_peso_imcActionPerformed

    private void btn_dica_do_dia_peso_imcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dica_do_dia_peso_imcActionPerformed
        if(f_data_calendar_peso_imc.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Gere os dados do dia escolhido no calendário","Dica do Dia", HEIGHT);
        }else{
            dica_do_dia(new java.sql.Date(calendar_peso_imc.getDate().getTime()));
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_dica_do_dia_peso_imcActionPerformed

    private void btn_gerar_grafico_ps_pdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_gerar_grafico_ps_pdMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_gerar_grafico_ps_pdMouseClicked

    private void btn_gerar_grafico_ps_pdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_gerar_grafico_ps_pdActionPerformed
        this.atualizar_graficos_progressao_ps_pd();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_gerar_grafico_ps_pdActionPerformed

    private void brn_limpar_progressao_ps_pdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brn_limpar_progressao_ps_pdActionPerformed
        Utilitarios.limparField(panel_progressao_ps_pd);
        // TODO add your handling code here:
    }//GEN-LAST:event_brn_limpar_progressao_ps_pdActionPerformed

    private void btn_gerar_dados_ps_pdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_gerar_dados_ps_pdActionPerformed
        try {
            this.get_dados_day_calendar_ps_pd(new java.sql.Date(this.sdf.parse(this.sdf.format(calendar_ps_pd.getDate())).getTime()));
            // TODO add your handling code here:
        } catch (ParseException ex) {
            Logger.getLogger(MainUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btn_gerar_dados_ps_pdActionPerformed

    private void btn_dica_do_dia_ps_pdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dica_do_dia_ps_pdActionPerformed
        if(f_data_calendar_ps_pd.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Gere os dados do dia escolhido no calendário","Dica do Dia", HEIGHT);
        }else{
            this.dica_do_dia(new java.sql.Date(calendar_ps_pd.getDate().getTime()));
        }
    }//GEN-LAST:event_btn_dica_do_dia_ps_pdActionPerformed

    private void panel_btn_medicosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_medicosMouseEntered
        Utilitarios.changeBackground_entered(panel_btn_medicos);
        // TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_medicosMouseEntered

    private void panel_btn_medicosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_medicosMouseExited
        Utilitarios.changeBackground_exited(panel_btn_medicos);

        // TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_medicosMouseExited

    private void panel_btn_avaliacao_diariaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_avaliacao_diariaMouseEntered
        Utilitarios.changeBackground_entered(panel_btn_avaliacao_diaria);
        // TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_avaliacao_diariaMouseEntered

    private void panel_btn_avaliacao_diariaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_avaliacao_diariaMouseExited
        Utilitarios.changeBackground_exited(panel_btn_avaliacao_diaria);

        // TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_avaliacao_diariaMouseExited

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseEntered

    private void panel_btn_informativoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_informativoMouseEntered
        Utilitarios.changeBackground_entered(panel_btn_informativo);
        // TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_informativoMouseEntered

    private void panel_btn_informativoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_informativoMouseExited
        Utilitarios.changeBackground_exited(panel_btn_informativo);
// TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_informativoMouseExited

    private void panel_btn_sairMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_sairMouseEntered
        Utilitarios.changeBackground_entered(panel_btn_sair);
        // TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_sairMouseEntered

    private void panel_btn_sairMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_sairMouseExited
        Utilitarios.changeBackground_exited(panel_btn_sair);
        // TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_sairMouseExited

    private void label_title_chart_peso_imcMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_title_chart_peso_imcMouseEntered
        Utilitarios.changeForeground_entered(label_title_chart_peso_imc);
        // TODO add your handling code here:
    }//GEN-LAST:event_label_title_chart_peso_imcMouseEntered

    private void label_title_chart_peso_imcMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_title_chart_peso_imcMouseExited
        Utilitarios.changeForeground_exited(label_title_chart_peso_imc);
        // TODO add your handling code here:
    }//GEN-LAST:event_label_title_chart_peso_imcMouseExited

    private void label_title_chart_ps_pdMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_title_chart_ps_pdMouseEntered
        Utilitarios.changeForeground_entered(label_title_chart_ps_pd);
        // TODO add your handling code here:
    }//GEN-LAST:event_label_title_chart_ps_pdMouseEntered

    private void label_title_chart_ps_pdMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_title_chart_ps_pdMouseExited
        Utilitarios.changeForeground_exited(label_title_chart_ps_pd);
        // TODO add your handling code here:
    }//GEN-LAST:event_label_title_chart_ps_pdMouseExited

    private void label_title_chart_peso_imcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_title_chart_peso_imcMouseClicked
        Utilitarios.atualizarPainel(panel_desk, panel_progressao_peso_imc);
        Utilitarios.changeForeground_exited(label_title_chart_peso_imc);
        this.atualizar_graficos_progressao_imc_peso();
        // TODO add your handling code here:
    }//GEN-LAST:event_label_title_chart_peso_imcMouseClicked

    private void label_title_chart_ps_pdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_title_chart_ps_pdMouseClicked
        this.atualizar_graficos_progressao_ps_pd();
        Utilitarios.changeForeground_exited(label_title_chart_ps_pd);
        Utilitarios.atualizarPainel(panel_desk,panel_progressao_ps_pd);
    }//GEN-LAST:event_label_title_chart_ps_pdMouseClicked

    private void panel_btn_homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_homeMouseClicked
        Utilitarios.limparField(panel_desk);
        Utilitarios.atualizarPainel(panel_desk, panel_desk_main);
        atualizarSistema();
        // TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_homeMouseClicked

    private void panel_btn_homeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_homeMouseEntered
        Utilitarios.changeBackground_entered(panel_btn_home);
        // TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_homeMouseEntered

    private void panel_btn_homeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_homeMouseExited
        Utilitarios.changeBackground_exited(panel_btn_home);
        // TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_homeMouseExited

    private void btn_cadastrar_medicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cadastrar_medicoActionPerformed
        Utilitarios.atualizarPainel(panel_desk, panel_registrar_medico);
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cadastrar_medicoActionPerformed

    private void btn_excluir_medicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_excluir_medicoActionPerformed
        if(tb_medicos.getSelectedRow() != -1){
            String CRA = tb_medicos.getValueAt(tb_medicos.getSelectedRow(),1).toString();
            User_DocDAO user_doc = new User_DocDAO();
            user_doc.setDoctor_CRA(CRA);
            user_doc.setUser_login(this.user.getLogin());
            user_doc.delete_user_doc(user_doc);
            atualizarSistema();
        }else{
            JOptionPane.showMessageDialog(null,"Selecione algum doutor antes de tentar excluí-lo");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_excluir_medicoActionPerformed

    private void panel_btn_contaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_contaMouseClicked
        Utilitarios.atualizarPainel(panel_desk, panel_perfil_usuario);
        atualizarCamposUsuario();
        // TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_contaMouseClicked

    private void panel_btn_contaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_contaMouseEntered
        Utilitarios.changeBackground_entered(panel_btn_conta);
        // TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_contaMouseEntered

    private void panel_btn_contaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_btn_contaMouseExited
        Utilitarios.changeBackground_exited(panel_btn_conta);
        // TODO add your handling code here:
    }//GEN-LAST:event_panel_btn_contaMouseExited

    private void btn_palterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_palterarActionPerformed
        atualizarCamposAUsuario();
        Utilitarios.limparField(panel_perfil_usuario);
        Utilitarios.atualizarPainel(panel_desk, panel_atualizar_usuario);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_palterarActionPerformed

    private void btn_aalterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_aalterarActionPerformed
        if(Utilitarios.validarCampos(panel_atualizar_usuario) && Utilitarios.validarEmail(f_aemail.getText())){
            if(f_asenha.getText().equals(user.getPass())){
                String caminho_novo = null;
                try {
                    caminho_novo  = Utilitarios.copiarFoto(caminho_foto,f_anome.getText());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,ex);
                }
                UserDAO user = new UserDAO();
                user.setLogin(f_alogin.getText());
                user.setPass(f_asenha_nova.getText());
                user.setName(f_anome.getText());
                user.setSmoke_frequency_per_week(Utilitarios.indiceSemanal((String) box_afumos.getSelectedItem()));
                user.setAlcool_frequency_per_week(Utilitarios.indiceSemanal((String) box_aalcool.getSelectedItem()));
                user.setExercise_frequency_per_week(Utilitarios.indiceSemanal((String) box_aexercise.getSelectedItem()));
                user.setHeight(Float.parseFloat(f_aaltura.getText()));
                user.setSexo((String) box_asexo.getSelectedItem());
                user.setEmail(f_aemail.getText());
                user.setUrl_foto(caminho_novo);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    java.sql.Date data = new java.sql.Date(sdf.parse(f_adata.getText()).getTime());
                    user.setData_nascimento(data);
                } catch (ParseException ex) {
                    Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
                }
                user.update_user(user);
                this.user = UserDAO.get_user(user.getLogin());
                Utilitarios.limparField(panel_atualizar_usuario);
                Utilitarios.atualizarPainel(panel_desk, panel_desk_main);
                atualizarSistema();
                
                }else{
                JOptionPane.showMessageDialog(null, "Senha Incorreta !!");
            }
        }
        
    }//GEN-LAST:event_btn_aalterarActionPerformed

    private void btn_alimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_alimparActionPerformed
        Utilitarios.limparField(panel_atualizar_usuario);
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_alimparActionPerformed

    private void l_afotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_afotoMouseClicked
        caminho_foto = Utilitarios.buscarFoto(l_afoto);
        // TODO add your handling code here:
    }//GEN-LAST:event_l_afotoMouseClicked

    private void box_asexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_box_asexoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_box_asexoActionPerformed

    private void box_psexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_box_psexoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_box_psexoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> box_aalcool;
    private javax.swing.JComboBox<String> box_aexercise;
    private javax.swing.JComboBox<String> box_afumos;
    private javax.swing.JComboBox<String> box_alcool;
    private javax.swing.JComboBox<String> box_alcool1;
    private javax.swing.JComboBox<String> box_asexo;
    private javax.swing.JComboBox<String> box_exercicios;
    private javax.swing.JComboBox<String> box_exercise;
    private javax.swing.JComboBox<String> box_fastfood;
    private javax.swing.JComboBox<String> box_fumos;
    private javax.swing.JComboBox<String> box_psexo;
    private javax.swing.JComboBox<String> box_smoke;
    private javax.swing.JButton brn_limpar_panel_peso_imc;
    private javax.swing.JButton brn_limpar_progressao_ps_pd;
    private javax.swing.JButton btn_aalterar;
    private javax.swing.JButton btn_alimpar;
    private javax.swing.JButton btn_cadastrar_medico;
    private javax.swing.JButton btn_dica_do_dia_peso_imc;
    private javax.swing.JButton btn_dica_do_dia_ps_pd;
    private javax.swing.JButton btn_excluir_medico;
    private javax.swing.JButton btn_gerar_dados_peso_imc;
    private javax.swing.JButton btn_gerar_dados_ps_pd;
    private javax.swing.JButton btn_gerar_grafico_peso_imc;
    private javax.swing.JButton btn_gerar_grafico_ps_pd;
    private javax.swing.JButton btn_palterar;
    private javax.swing.JButton btn_registrar_aval;
    private javax.swing.JButton btn_registrar_medico;
    private javax.swing.JButton btn_voltar_aval;
    private javax.swing.JButton btn_voltar_medico;
    private com.toedter.calendar.JCalendar calendar_peso_imc;
    private com.toedter.calendar.JCalendar calendar_ps_pd;
    private javax.swing.JTextField f_CRA;
    private javax.swing.JFormattedTextField f_aaltura;
    private javax.swing.JFormattedTextField f_adata;
    private javax.swing.JTextField f_aemail;
    private javax.swing.JTextField f_alogin;
    private javax.swing.JTextField f_anome;
    private javax.swing.JPasswordField f_asenha;
    private javax.swing.JPasswordField f_asenha_nova;
    private javax.swing.JTextField f_data_calendar_peso_imc;
    private javax.swing.JTextField f_data_calendar_ps_pd;
    private javax.swing.JFormattedTextField f_data_fim_grafico_peso_imc;
    private javax.swing.JFormattedTextField f_data_fim_grafico_ps_pd;
    private javax.swing.JFormattedTextField f_data_inicio_grafico_peso_imc;
    private javax.swing.JFormattedTextField f_data_inicio_grafico_ps_pd;
    private javax.swing.JTextField f_diastolic_pressure;
    private javax.swing.JTextField f_email_medico;
    private javax.swing.JTextField f_imc_calendar;
    private javax.swing.JTextField f_nome_medico;
    private javax.swing.JFormattedTextField f_paltura;
    private javax.swing.JTextField f_pd_calendar;
    private javax.swing.JFormattedTextField f_pdata;
    private javax.swing.JTextField f_pemail;
    private javax.swing.JTextField f_peso_calendar;
    private javax.swing.JTextField f_plogin;
    private javax.swing.JTextField f_pnome;
    private javax.swing.JTextField f_ps_calendar;
    private javax.swing.JPasswordField f_psenha;
    private javax.swing.JPasswordField f_senha_aval;
    private javax.swing.JPasswordField f_senha_medico;
    private javax.swing.JTextField f_systolic_pressurel;
    private javax.swing.JTextField f_weight;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel l_afoto;
    private javax.swing.JLabel l_pfoto;
    private javax.swing.JLabel label555;
    private javax.swing.JLabel label_altura;
    private javax.swing.JLabel label_avaliacao_diaria;
    private javax.swing.JLabel label_class_imc_recente;
    private javax.swing.JLabel label_class_pressao_recente;
    private javax.swing.JLabel label_classification_imc;
    private javax.swing.JLabel label_foto;
    private javax.swing.JLabel label_home;
    private javax.swing.JLabel label_home1;
    private javax.swing.JLabel label_idade;
    private javax.swing.JLabel label_imc_recente;
    private javax.swing.JLabel label_informativo;
    private javax.swing.JLabel label_nome;
    private javax.swing.JLabel label_pd_recente;
    private javax.swing.JLabel label_peso_aval;
    private javax.swing.JLabel label_peso_aval1;
    private javax.swing.JLabel label_peso_aval2;
    private javax.swing.JLabel label_peso_aval3;
    private javax.swing.JLabel label_peso_aval4;
    private javax.swing.JLabel label_peso_aval5;
    private javax.swing.JLabel label_peso_recente;
    private javax.swing.JLabel label_pressao_d_aval;
    private javax.swing.JLabel label_pressao_s_aval;
    private javax.swing.JLabel label_ps_recente;
    private javax.swing.JLabel label_registrar_medico;
    private javax.swing.JLabel label_registro_medico;
    private javax.swing.JLabel label_registro_medico1;
    private javax.swing.JLabel label_sair;
    private javax.swing.JLabel label_title_chart_peso_imc;
    private javax.swing.JLabel label_title_chart_ps_pd;
    private javax.swing.JPanel panel_atualizar_usuario;
    private javax.swing.JPanel panel_avaliacao_diaria;
    private javax.swing.JPanel panel_btn_avaliacao_diaria;
    private javax.swing.JPanel panel_btn_conta;
    private javax.swing.JPanel panel_btn_home;
    private javax.swing.JPanel panel_btn_informativo;
    private javax.swing.JPanel panel_btn_medicos;
    private javax.swing.JPanel panel_btn_progressao_peso_imc;
    private javax.swing.JPanel panel_btn_progressao_ps_pd;
    private javax.swing.JPanel panel_btn_sair;
    private javax.swing.JPanel panel_calendar_data;
    private javax.swing.JPanel panel_calendar_ps_pd_data;
    private javax.swing.JPanel panel_desk;
    private javax.swing.JPanel panel_desk_main;
    private javax.swing.JPanel panel_desk_main_desk;
    private javax.swing.JPanel panel_desk_main_head;
    private javax.swing.JPanel panel_grafico_peso_imc;
    private javax.swing.JPanel panel_grafico_peso_imc_datado;
    private javax.swing.JPanel panel_grafico_ps_pd;
    private javax.swing.JPanel panel_grafico_ps_pd_datado;
    private javax.swing.JPanel panel_head;
    private javax.swing.JPanel panel_labels_head;
    private javax.swing.JPanel panel_main;
    private javax.swing.JPanel panel_medicos;
    private javax.swing.JPanel panel_perfil_usuario;
    private javax.swing.JPanel panel_progressao_peso_imc;
    private javax.swing.JPanel panel_progressao_peso_imc_conteiner_chart;
    private javax.swing.JPanel panel_progressao_ps_pd;
    private javax.swing.JPanel panel_progressao_ps_pd_conteiner_chart;
    private javax.swing.JPanel panel_registrar_medico;
    private javax.swing.JTable tb_medicos;
    // End of variables declaration//GEN-END:variables
}
