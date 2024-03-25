package com.gisnet.erpp.job;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.service.AnalisisPorSistemaService;
import com.gisnet.erpp.service.PrelacionService;

import net.sf.jasperreports.engine.JRException;

@Service
public class AnalisisPorSistemaJob {	 
	 private final Logger log = LoggerFactory.getLogger(this.getClass());
	 
	 @Autowired
	 AnalisisPorSistemaService analisisPorSistemaService;
	 
	 @Autowired
	 PrelacionService prelacionService;
	 
	
	 public void procesar() throws JRException, MessagingException {
		 //TODO borrar metodo
		 //analisisPorSistemaService.procesar();
		 prelacionService.prelacionesAtrasadas();
	 }
	//JADV-SUSPENSION
	 public void DepurarPrelacionesSuspendidas() throws JRException, MessagingException {

		 prelacionService.CronDepurarPrelacionesSuspendidas();
		 
	 }
		 

}
