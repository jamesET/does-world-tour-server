package com.justjames.beertour.mail;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEnvironmentPreparedEventListenter {
	
	private Log log = LogFactory.getLog(ApplicationEnvironmentPreparedEventListenter.class);
	
	@Autowired
	SendEmail sender;
	
	@Value("${notify.email}")
	String notifyEmail;

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (StringUtils.isNotBlank(notifyEmail)) {
			log.info("Sending startup notification to " + notifyEmail);
			sender.safeSendMail(notifyEmail, "Beer Tour Started", "Ay buddy, the Beer Tour Application started!");
		} else {
			log.info("Not sending notification, 'notify.email' property is not set");
		}
		
	}

}
