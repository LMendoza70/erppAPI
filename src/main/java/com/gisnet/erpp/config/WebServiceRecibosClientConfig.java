package com.gisnet.erpp.config;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

import org.apache.ws.security.WSConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

import com.gisnet.erpp.ws.firma.client.FirmaElectronicaClient;
import com.gisnet.erpp.ws.recibos.client.RecibosClient;

@Configuration
public class WebServiceRecibosClientConfig {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${reciboswebservice.uri}")
	private String uri;

	
    @Bean
    public Jaxb2Marshaller getMarshallerRecibos(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.gisnet.erpp.ws.recibos.client");
        return marshaller;
    }
    
    @Bean
    public WebServiceMessageFactory webServiceMessageFactory() throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        WebServiceMessageFactory webMessageFactory = new SaajSoapMessageFactory(messageFactory);
        return webMessageFactory;
    }

    @Bean
    public RecibosClient getRecibosClient() throws SOAPException{
    	RecibosClient recibosClient = new RecibosClient();
    	recibosClient.setMarshaller(getMarshallerRecibos());
    	recibosClient.setUnmarshaller(getMarshallerRecibos());
    	recibosClient.setDefaultUri(uri);        
    	recibosClient.setMessageFactory(webServiceMessageFactory());
        return recibosClient;
    }
}
