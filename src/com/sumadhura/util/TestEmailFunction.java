package com.sumadhura.util;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


//@Controller
public class TestEmailFunction {/*
 
	//@Autowired
   //private  SalesReport sr;
	//@Autowired
	//private SalesReportDetails srdtls;
	
	 
	
	
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final String SMTP_PORT = "465";
    private static final String emailMsgTxt = "this mail demo for v project";
    private static final String emailSubjectTxt = "this for my project";
    private static final String emailFromAddress = "pavan45662@gmail.com";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final String[] sendTo = { "cherukuvinod90@gmail.com","pavan45662@gmail.com" };
 
    public static void main(String [] args) throws Exception {
 
    	
    	 
    	
    	
      //  Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        TestEmailFunction sendMailThroughJava = new TestEmailFunction();
        sendMailThroughJava.sendSSLMessage(sendTo, emailSubjectTxt,
                emailMsgTxt, emailFromAddress);
        System.out.println("Sucessfully sent mail to all Users");
    }
 
  
    public void sendSSLMessage(String recipients[], String subject,
            String message, String from) throws MessagingException {
        boolean debug = true;
 
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");
 
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("pavan45662@gmail.com",
                                "password");
                    }
                });
 
        session.setDebug(debug);
 
        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);
        //String fileName="c:/text/name";
 
        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
 
        // Setting the Subject and Content Type
        
        
        
        
        BodyPart messageBodyPart = new MimeBodyPart();

        // Now set the actual message
        messageBodyPart.setText("vinod this is for you");

        // Create a multipar message
        Multipart multipart = new MimeMultipart();

        // Set text message part
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();   
        //String filename = "F:/SalesReport.xls";
       // DataSource source = new FileDataSource(filename);
       // messageBodyPart.setDataHandler(new DataHandler(source));
       // messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);

        // Send the complete message parts
     

        // Send message
       // Transport.send(message);

        
       msg.setSubject(subject);
       msg.setContent(multipart);
       // msg.setFileName(fileName);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
    }
    
    
*/}