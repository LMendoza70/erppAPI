package com.gisnet.erpp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.ws.referencia.client.Referencia;
import com.gisnet.erpp.ws.referencia.client.ReferenciaClient;

@Service
public class ReferenciaWebService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${reciboswebservice.datetimeformat}")
	private String RECIBOS_WEB_SERVICE_FORMAT;

	@Autowired
	ReferenciaClient referenciaClient;

	public Referencia getConsulta(String referencia) {
		return referenciaClient.getConsulta(referencia);
	}

}
