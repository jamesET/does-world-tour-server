package com.justjames.beertour.mail;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SendEmail {
	
	private Log log = LogFactory.getLog(SendEmail.class);
	
    // SMTP credentials below
    @Value("${smtp.username}")
    private String smtpUsername;
    
    @Value("${smtp.password}")
    private String smtpPassword;
    
    // SMTP host name
    @Value("${smtp.host}")
    private String smtpHost = "email-smtp.us-west-2.amazonaws.com";    
    
    //  SMTP port
    @Value("${smtp.port}")
    private Integer smtpPort = 25;
    
    @Value("${system.email}")
    private String smtpFromAddress;
    
    // Mail session
    private Session session;
    
    private boolean mailEnabled = true;
    
    public SendEmail() {
    	
        // Create a Properties object to contain connection configuration information.
     	Properties props = System.getProperties();
     	props.put("mail.transport.protocol", "smtp");
     	props.put("mail.smtp.port", smtpPort); 
     	
     	// Set properties indicating that we want to use STARTTLS to encrypt the connection.
     	// The SMTP session will begin on an unencrypted connection, and then the client
         // will issue a STARTTLS command to upgrade to an encrypted connection.
     	props.put("mail.smtp.auth", "true");
     	props.put("mail.smtp.starttls.enable", "true");
     	props.put("mail.smtp.starttls.required", "true");

         // Create a Session object to represent a mail session with the specified properties. 
     	session = Session.getDefaultInstance(props);
    	
    }
    
    public void sendMail(String to, String subject,String body) {
    	Transport transport = null;
    	
    	if (!isMailEnabled()) {
    		log.info("mail is disabled, no mail sent to " + to);
    		return;
    	}

    	try {
    		// Create a message with the specified information. 
    		MimeMessage msg = new MimeMessage(session);
    		msg.setFrom(new InternetAddress(smtpFromAddress));
    		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
    		msg.setSubject(subject);
    		msg.setContent(body,"text/plain");

    		// Create a transport.        
    		transport = session.getTransport();
    		
            // Connect to SMTP server
            transport.connect(smtpHost, smtpUsername, smtpPassword);
        	
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());

    	}
    	catch (Exception ex) {
    		log.error(String.format("Error sending email from '%s' to '%s' ... %s => %s",
    				smtpFromAddress, to, ex.getClass().getName(),ex.getMessage()));
    	}
    	finally {
    		
    		if (transport != null) {
    			try {
    				// Close and terminate the connection.
    				transport.close();
    			} catch (Exception ex) {
    				log.error("SMTP connection failed to close.  " + ex.getMessage());
    			}
    		}
    	}
    	
    }

	private boolean isMailEnabled() {
		
    	if (StringUtils.isBlank(smtpUsername)) {
    		setMailEnabled(false);
    		log.info("smtp.username is not set, email disabled");
    	}

    	if (StringUtils.isBlank(smtpPassword)) {
    		setMailEnabled(false);
    		log.info("smtp.password is not set, email disabled");
    	}

    	if (StringUtils.isBlank(smtpHost)) {
    		setMailEnabled(false);
    		log.info("smtp.host is not set, email disabled");
    	}

    	if (StringUtils.isBlank(smtpFromAddress)) {
    		setMailEnabled(false);
    		log.info("system.email is not set, email disabled");
    	}
    	
    	if (smtpPort <= 0) {
    		setMailEnabled(false);
    		log.info("smtp.port must be > 0, email disabled");
    	}
		
		return mailEnabled;
	}

	private void setMailEnabled(boolean mailEnabled) {
		this.mailEnabled = mailEnabled;
	}
    
    
 
}
