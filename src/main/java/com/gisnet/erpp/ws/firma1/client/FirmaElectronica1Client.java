package com.gisnet.erpp.ws.firma1.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.web.api.firma.FirmaRequestDTO;

@Service
public class FirmaElectronica1Client {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${firmasgsigntoolswswebservice.uri}")
	private String FIRMA_WEB_SERVICE_URI;
	
	@Value("${firmasgsigntoolswswebservice.user}")
	private String FIRMA_WEB_SERVICE_USER;
	
	@Value("${firmasgsigntoolswswebservice.password}")
	private String FIRMA_WEB_SERVICE_PASSWORD;
	
	
	public String registrarFirmaEnServicioEstatal(FirmaRequestDTO firmaRequestDTO) {
		log.info("Iniciando proceso para {} con pkcs7={}", firmaRequestDTO.getOriginal(), firmaRequestDTO.getPkcs7());
		
		SgSignToolsWS service = null;
		try {
			service = new SgSignToolsWS(new URL(FIRMA_WEB_SERVICE_URI));
		} catch (MalformedURLException e) {
			log.error(e.getMessage(), e);
		}
		SgSignToolsWSPortType port = service.getSgSignToolsWS();
				
		/*
		 * MultilateralInit
		 */
		Document originalDoc = new Document();						
		originalDoc.setFilename("original");
		originalDoc.setCompressed(false);
		originalDoc.setData(firmaRequestDTO.getOriginal().getBytes());
		
		
		MultilateralInitRequest multilateralInitRequest = new MultilateralInitRequest();
		multilateralInitRequest.setDocToSign(originalDoc);
		multilateralInitRequest.setDatatype(DataType.DOCUMENT);
		
						 
		Map<String, Object> reqCtx = ((BindingProvider)port).getRequestContext();

		reqCtx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, FIRMA_WEB_SERVICE_URI);
		reqCtx.put(BindingProvider.USERNAME_PROPERTY, FIRMA_WEB_SERVICE_USER);
		reqCtx.put(BindingProvider.PASSWORD_PROPERTY, FIRMA_WEB_SERVICE_PASSWORD);
     

		MultilateralInitResponse multilateralInitResponse = (MultilateralInitResponse) port.processMessage(multilateralInitRequest);
		
		String processId = multilateralInitResponse.getId();
		
		log.info("ProcesoId = {}, Digest={}", processId,multilateralInitResponse.getDigest());

		/*
		 * MultilateralUpdate
		 */
		Document doc = new Document();
		doc.setFilename(" ");
		doc.setCompressed(false);
		doc.setData(firmaRequestDTO.getPkcs7().getBytes());
		
		MultilateralUpdateRequest multilateralUpdateRequest = new MultilateralUpdateRequest();
		multilateralUpdateRequest.setIdFromInit(processId);
		multilateralUpdateRequest.setDocumentSignedWithoutContent(doc);
		
		MultilateralUpdateResponse multilateralUpdateResponse = (MultilateralUpdateResponse) port.processMessage(multilateralUpdateRequest);
		
		String secuencia = multilateralUpdateResponse.getSequenceOfTransaction();
		
		log.info("secuencia = {}", secuencia);
		
		/*
		 * MultilateralFinalize
		 */	

		log.info("Cerrando proceso");
		MultilateralFinalizeRequest multilateralFinalizeRequest = new MultilateralFinalizeRequest();
		multilateralFinalizeRequest.setIdFromInit(processId);
		multilateralFinalizeRequest.setKeepOpen(false);
		
		MultilateralFinalizeResponse multilateralFinalizeResponse = (MultilateralFinalizeResponse) port.processMessage(multilateralFinalizeRequest);
		
		return secuencia;	
	}
}
