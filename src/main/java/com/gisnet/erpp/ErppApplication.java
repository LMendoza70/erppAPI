package com.gisnet.erpp;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * = ErppApplication
 *
 * TODO Auto-generated class documentation
 *
 */

@SpringBootApplication
@EnableScheduling
public class ErppApplication extends SpringBootServletInitializer {

    /**
     * TODO Auto-generated method documentation
     *
     * @param args
     */
	
	//CAMBIO TRABAJO
    public static void main(String[] args) {
        SpringApplication.run(applicationClass, args);
    }
  
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }
    
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Mexico_City"));
    }
    

    
    //BUG FIX LISTO
    private static Class<ErppApplication> applicationClass = ErppApplication.class;
}