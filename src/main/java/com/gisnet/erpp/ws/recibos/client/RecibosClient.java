package com.gisnet.erpp.ws.recibos.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class RecibosClient extends WebServiceGatewaySupport {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public String obtieneDatosGral(String estadoCuenta) {
		log.info("Llamando web service Recibos.ObtieneDatosGral con " + estadoCuenta);
		
		ObjectFactory factory = new ObjectFactory();
		ObtieneDatosGral request = new ObtieneDatosGral();
		request.setStrEstadoCuenta(estadoCuenta);
		ObtieneDatosGralResponse response = (ObtieneDatosGralResponse) getWebServiceTemplate().marshalSendAndReceive(request);
		return response.getObtieneDatosGralResult();
	}

	public String obtieneDatosCobro(String estadoCuenta) {
		log.info("Llamando web service Recibos.ObtieneDatosCobro con " + estadoCuenta);

		ObjectFactory factory = new ObjectFactory();
		ObtieneDatosCobro request = new ObtieneDatosCobro();
		request.setStrEstadoCuenta(estadoCuenta);

		ObtieneDatosCobroResponse response = (ObtieneDatosCobroResponse) getWebServiceTemplate().marshalSendAndReceive(request);
		return response.getObtieneDatosCobroResult();
	}
	
	

}