package com.gisnet.erpp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.gisnet.erpp.service.AccesoService;

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private AccesoService accesoService;
		
    @Override 
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	accesoService.cerrarSesionesAbiertas();
    }
}
