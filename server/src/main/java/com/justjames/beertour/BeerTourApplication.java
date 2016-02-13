package com.justjames.beertour;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class BeerTourApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BeerTourApplication.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	return application.sources(applicationClass);
    }

    private static Class<BeerTourApplication> applicationClass = BeerTourApplication.class;
    
}
