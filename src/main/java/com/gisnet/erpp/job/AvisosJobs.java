package com.gisnet.erpp.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.service.AvisoService;


@Service
public class AvisosJobs {
	private final AvisoService avisoService;
	private final ActoService actoService;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	public AvisosJobs(AvisoService avisoService, ActoService actoService) {
		this.avisoService = avisoService;
		this.actoService = actoService;
	}
	//
	@Scheduled(cron = "0 0 0/6 * * ?", zone = "America/Mexico_City")
	public void cancelarActosCaducados() {
		List<Acto> actosCaducados = avisoService.buscarActosCaducados();
		System.out.println("BUSQUEDA DE ACTOS: "+actosCaducados.size());
		actoService.cancelarActos(actosCaducados);
	}

}
