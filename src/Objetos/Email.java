package Objetos;

import Objetos.Day_atualization;
import Objetos.Doctor;
import Objetos.User;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
 
public class Email
{
    private static final String GMAIL = "healthsafepi@gmail.com";
    private static final String PASSWORD = "123456@8";
    private String message;
    private String title;
    private String destinatario;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public Email() {
    }
    
    public static String buildMessage(User user,Day_atualization day_atua){
        String message = null;
        message = 
                "#Olá, o paciênte com nome de:\n"
                + "#"+user.getName() +"\n"
                + "#Email:#\n "
                + "#"+user.getEmail()+".\n"
                + "#Apresentou a seguinte classificação de IMC: \n"
                + "#"+day_atua.getClassification_imc()+"\n"
                + "#E a seguinte classificação de Pressão: \n"
                + "#" + day_atua.getClassification_pressure()
                +"  " + day_atua.getSystolic_pressure() + "x" + day_atua.getDistolic_pressure()
                + "\n#No Dia "
                + sdf.format(new java.util.Date())
                +"\n"
                +"#HealthSafe <3"; 
    
    
        return message;
    }
    
    public static Session getConnectionGmail(){
        /** Parâmetros de conexão com servidor Gmail */
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", 
        "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props,
        new javax.mail.Authenticator() {
           protected PasswordAuthentication getPasswordAuthentication() 
           {
                 return new PasswordAuthentication(GMAIL, 
                 PASSWORD);
           }
         });
        
        /** Ativa Debug para sessão */
        session.setDebug(true);
        return session;
    }
    
    public static void sendEmail(Email email) {
      Session session = getConnectionGmail();
        try {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(GMAIL)); 

        Address[] toUser = InternetAddress.parse(email.getDestinatario());  

        message.setRecipients(Message.RecipientType.TO, toUser);
        message.setSubject(email.getTitle());
        message.setText(email.getMessage());

        Transport.send(message);
        System.out.println("Feito!!!");
         } catch (MessagingException e) {
             JOptionPane.showMessageDialog(null,"O email não foi enviado para os doutores registrados, verifique sua conexão com a internet");
        }
    }
}