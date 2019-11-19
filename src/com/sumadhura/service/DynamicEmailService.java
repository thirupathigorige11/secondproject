package com.sumadhura.service;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.sumadhura.util.UIProperties;

public class DynamicEmailService extends UIProperties{

	public JavaMailSenderImpl getMailSender(String hostEmail,String hostEmailPassword){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(465);
		mailSender.setUsername(hostEmail);
		mailSender.setPassword(hostEmailPassword);

		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.debug", "true");
		javaMailProperties.put("mail.smtp.socketFactory.port",465);
		javaMailProperties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		javaMailProperties.put("mail.smtp.socketFactory.fallback", "false");
		
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}
	

	
	private MimeMessagePreparator getMessagePreparator(final String recipients[], final String subject,
			final String message, final String from, final String ccRecipients[]) throws MessagingException {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
            	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            	//MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            	
            	helper.setFrom(from);
            	helper.setTo(recipients);
            	helper.setCc(ccRecipients);
                helper.setSubject(subject);
               	
            	helper.setText(message,true);
            	//mimeMessage.setContent(message, "text/html");
            	

            }
        };

		return preparator;
	}
	
	public void sendEmployeeEmail(String emailBodyMsgText,String emailSubjectText,String emailFromAddress,String [] emailToAddress,String[] ccTo,String strIndentSiteId) throws MessagingException{
		String hostEmail = validateParams.getProperty("HOST_EMAIL_EMPLOYEE");
		String hostEmailPassword = validateParams.getProperty("HOST_EMAIL_EMPLOYEE_PASSWORD");
		JavaMailSender mailSender = getMailSender(hostEmail,hostEmailPassword);
		MimeMessagePreparator preparator = getMessagePreparator(emailToAddress, emailSubjectText,emailBodyMsgText, emailFromAddress, ccTo);
		mailSender.send(preparator);
		
	}
	public void sendVendorEmail(String emailBodyMsgText,String emailSubjectText,String emailFromAddress,String [] emailToAddress,String[] ccTo,String strIndentSiteId) throws MessagingException{
		String hostEmail = validateParams.getProperty("HOST_EMAIL_VENDOR");
		String hostEmailPassword = validateParams.getProperty("HOST_EMAIL_VENDOR_PASSWORD");
		JavaMailSender mailSender = getMailSender(hostEmail,hostEmailPassword);
		MimeMessagePreparator preparator = getMessagePreparator(emailToAddress, emailSubjectText,emailBodyMsgText, emailFromAddress, ccTo);
		mailSender.send(preparator);
		
		
	}


	
}
