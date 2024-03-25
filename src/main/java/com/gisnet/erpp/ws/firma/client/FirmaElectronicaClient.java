package com.gisnet.erpp.ws.firma.client;

import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.gisnet.erpp.web.api.firma.FirmaRequestDTO;

public class FirmaElectronicaClient extends WebServiceGatewaySupport {
	private final static int ACCION_CERRAR=1;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public String registrarFirmaEnServicioEstatal(FirmaRequestDTO firmaRequestDTO) {
		log.info("Iniciando proceso para " + firmaRequestDTO.getPkcs7());
		
		ObjectFactory factory = new ObjectFactory();
		
		MultiSignedMessageIn multiSignedMessageIn = new MultiSignedMessageIn();
		multiSignedMessageIn.setData(firmaRequestDTO.getOriginal().getBytes());
		multiSignedMessageIn.setDataType(0);
		multiSignedMessageIn.setFlags((byte) 2);
		multiSignedMessageIn.setDataInfo("Ejemplo Flujo de Firma");
		multiSignedMessageIn.setHashAlgorithm("SHA-256");
		multiSignedMessageIn.setSignersNum((short)1);
		

		MultiSignedMessageInit request =factory.createMultiSignedMessageInit();
		request.setMultiSignedMessageInRequest(multiSignedMessageIn);
			
		MultiSignedMessageInitResponse multiSignedMessageInitResponse = (MultiSignedMessageInitResponse) getWebServiceTemplate().marshalSendAndReceive(request);
		
		String processId = multiSignedMessageInitResponse.getMultiSignedMessageInitResponse().getProcessID();
		
		log.info("ProcesoId = {}, Hash={}", processId, multiSignedMessageInitResponse.getMultiSignedMessageInitResponse().getHash());
		
		SSignDocument cmsRequest= new SSignDocument();
		cmsRequest.setBase64(false);
		cmsRequest.setData(Base64.getDecoder().decode(firmaRequestDTO.getPkcs7().getBytes()));
		cmsRequest.setFileName(" ");
		
		MultiSignedMessageUpdate multiSignedMessageUpdate = new MultiSignedMessageUpdate();
		multiSignedMessageUpdate.setProcessIDRequest(processId);
		multiSignedMessageUpdate.setSignedMessageRequest(cmsRequest);
		multiSignedMessageUpdate.setSerialNumberRequest("111111111111111111111");
		
		MultiSignedMessageUpdateResponse multiSignedMessageUpdateResponse = (MultiSignedMessageUpdateResponse) getWebServiceTemplate().marshalSendAndReceive(multiSignedMessageUpdate);
		
		String secuencia = multiSignedMessageUpdateResponse.getMultiSignedMessageUpdateResponse();
		
		log.info("secuencia = {}", secuencia);
		
		log.info("Cerrando proceso");
		MultiSignedMessageFinal multiSignedMessageFinal = new MultiSignedMessageFinal();
		multiSignedMessageFinal.setProcessIDRequest(processId);
		multiSignedMessageFinal.setProcessActionRequest(ACCION_CERRAR);
		
		MultiSignedMessageFinalResponse multiSignedMessageFinalResponse = (MultiSignedMessageFinalResponse) getWebServiceTemplate().marshalSendAndReceive(multiSignedMessageFinal);
		
		List<SSignEvidence> signEvidence = multiSignedMessageFinalResponse.getMultiSignedMessageFinalResponse();
		
		for(SSignEvidence se: signEvidence) {
			log.info("data = {}, tipo de evidencia= {}, secuencia = {}", new String( se.getData()), se.getEvidenceType(), se.getSequence());
		} 
		
		return secuencia;
	}
}