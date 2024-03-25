package com.gisnet.erpp.service;

import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.util.UtilFecha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.web.api.firma.FirmaDTO;
import com.gisnet.erpp.web.api.firma.FirmaRequestDTO;
import com.gisnet.erpp.ws.firma.client.FirmaElectronicaClient;
import com.gisnet.erpp.ws.firma1.client.FirmaElectronica1Client;

@Service
public class FirmaService{
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${firmawebservice.datetimeformat}")
	private String FIRMA_WEB_SERVICE_FORMAT;
	
	@Autowired
	ParametroRepository parametroRepository;
	
	@Autowired
	FirmaElectronicaClient firmaElectronicaClient;	
	
	@Autowired
	FirmaElectronica1Client firmaElectronica1Client;	

	public String registrarFirmaEnServicioEstatal(FirmaRequestDTO firmaRequestDTO){
		String skip = parametroRepository.findByCve("SKIP_FIRMA").getValor();
		log.info("Skip Firma: {}",skip);
		if (!"true".equals(skip)) { 
			if (firmaRequestDTO == null || "SHA-1".equals(firmaRequestDTO.getAlgoritmo())) {
				return firmaElectronica1Client.registrarFirmaEnServicioEstatal(firmaRequestDTO);
			} else {
				return firmaElectronicaClient.registrarFirmaEnServicioEstatal(firmaRequestDTO);
			}
		} else {
			return "8493";
		}
	}
	
	public FirmaDTO parseEstampa(String estampa){
		return new FirmaDTO(estampa, FIRMA_WEB_SERVICE_FORMAT);
	}

	public FirmaDTO testFirmaEnServicio(String hash) {
		final FirmaDTO firma = new FirmaDTO();
		firma.setSecuencia(123456L);
		firma.setPolitica("1.3.6.1.4.1.9203.2.1");
		firma.setDigestionTS("4CF2531CEB5D75F5204DC65C33F6765D89DD17EB");
		firma.setSecuenciaTS(12500L);
		firma.setEstampilla( UtilFecha.toDateTime ("20170814161828", "yyyyMMddHHmmss"));

		return firma;
	}
}
