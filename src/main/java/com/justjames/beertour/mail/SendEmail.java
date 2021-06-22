package com.justjames.beertour.mail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendEmail {
	
	private Log log = LogFactory.getLog(SendEmail.class);
	
    @Value("${system.email}")
    private String smtpFromAddress;

    @Autowired
    private JavaMailSender mailSender;
    
    public void sendMail(String to, String subject,String body) {
    	
    	log.info(String.format("Sending email to '%s' ...",to));

    	try {            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(smtpFromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            
            mailSender.send(message);

    	}
    	catch (MailException ex) {
    		log.error(ex.getMessage());
    		log.error(String.format("Error sending email from '%s' to '%s' ... %s => %s",
    				smtpFromAddress, to, ex.getClass().getName(),ex.getMessage()));
    		throw new MailException("Unable to send mail");
    	}
    	
    	
    }

	
	public void safeSendMail(String to, String subject,String body) {
		try {
			sendMail(to,subject,body);
		} catch (Exception e) {
			// do nothing
		}
	}
    
 
}
