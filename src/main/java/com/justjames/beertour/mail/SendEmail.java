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
    private String smtpHost;
    
    //  SMTP port
    @Value("${smtp.port}")
    private Integer smtpPort = 0;
    
    @Value("${system.email}")
    private String smtpFromAddress;
    
    // Mail session
    private Session session;
    
    private boolean mailEnabled = true;
    
    private void initSession() {
    	
    	// Verify everything is setup
    	if (!isValidSMTPConfiguration()) {
    		return;
    	}
    	
    	// Only need to define session once
    	if (session != null) {
    		return;
    	}
    	
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
     	props.put("mail.smtp.connectiontimeout", 15000);


         // Create a Session object to represent a mail session with the specified properties. 
     	session = Session.getDefaultInstance(props);
    	
    }
    
    public void sendMail(String to, String subject,String body) {
    	Transport transport = null;
    	
    	initSession();
    	
    	if (!isValidSMTPConfiguration()) {
    		log.info("mail is disabled, no mail sent to " + to);
    		return;
    	} else {
    		log.info(String.format("Sending email to '%s' ...",to));
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
    		log.error(String.format("smtpHost='%s', smtpPort=%d smtpUserName='%s'",
    				smtpHost, smtpPort, smtpUsername));
    		log.error(String.format("Error sending email from '%s' to '%s' ... %s => %s",
    				smtpFromAddress, to, ex.getClass().getName(),ex.getMessage()));
    		throw new MailException("Unable to send mail");
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

	private boolean isValidSMTPConfiguration() {
		
    	if (StringUtils.isBlank(smtpUsername)) {
    		mailEnabled = false;
    		log.info("smtp.username is not set, email disabled");
    	}

    	if (StringUtils.isBlank(smtpPassword)) {
    		mailEnabled = false;
    		log.info("smtp.password is not set, email disabled");
    	}

    	if (StringUtils.isBlank(smtpHost)) {
    		mailEnabled = false;
    		log.info("smtp.host is not set, email disabled");
    	}

    	if (StringUtils.isBlank(smtpFromAddress)) {
    		mailEnabled = false;
    		log.info("system.email is not set, email disabled");
    	}
    	
    	if (smtpPort <= 0) {
    		mailEnabled = false;
    		log.info("smtp.port must be > 0, email disabled");
    	}
		
		return mailEnabled;
	}
	
	
	public void safeSendMail(String to, String subject,String body) {
		try {
			sendMail(to,subject,body);
		} catch (Exception e) {
			// do nothing
		}
	}
    
 
}
