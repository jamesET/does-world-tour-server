package com.justjames.beertour.beerlist;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoComplete {

    private static final Log log = LogFactory.getLog(AutoComplete.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    @Autowired
    BeerListSvc beerListSvc;


    /**
     * Complete unverified beers by 4pm every day 
     */
    @Scheduled(cron = "0 0 16 * * ?",zone = "America/Chicago") 
    public void autoComplete() {
    	beerListSvc.completeAllOutstanding();
    	
    }
}
