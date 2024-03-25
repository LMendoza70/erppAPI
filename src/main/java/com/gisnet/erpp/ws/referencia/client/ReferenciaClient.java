package com.gisnet.erpp.ws.referencia.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReferenciaClient {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${referenciawebservice.uri}")
	private String REFERENCIA_WEB_SERVICE_URI;

	public Referencia getConsulta(String referencia) {
		ReferenciaDPAService service = null;
		try {
			service = new ReferenciaDPAService(new URL(REFERENCIA_WEB_SERVICE_URI));
		} catch (MalformedURLException e) {
			log.error(e.getMessage(), e);
		}
		ReferenciaDPAEndpoint endPonint = service.getReferenciaDPAPort();
		return endPonint.getConsulta(referencia);
	}

}
