package com.gisnet.erpp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.util.Constantes;

@Service
public class EnvironmentService {
	@Autowired
	private Environment environment;
	
	public boolean isProduccion(){
		return environment!=null && environment.getActiveProfiles().length>0 && Constantes.PROD_ENVIRONMENT.equals(environment.getActiveProfiles()[0]);
	}

}
