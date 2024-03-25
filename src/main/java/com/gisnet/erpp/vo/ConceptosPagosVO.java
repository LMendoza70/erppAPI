package com.gisnet.erpp.vo;

import com.gisnet.erpp.web.api.cotizador.ConceptoPagoDTO;

public class ConceptosPagosVO {

	private ConceptoPagoDTO[] conceptosPago;
	private String documentoFundatorio;
	private String nombreRecibo;
	private String rfcRecibo;

	public ConceptoPagoDTO[] getConceptosPago() {
		return conceptosPago;
	}

	public void setConceptosPago(ConceptoPagoDTO[] conceptosPago) {
		this.conceptosPago = conceptosPago;
	}

	public String getDocumentoFundatorio() {
		return documentoFundatorio;
	}

	public void setDocumentoFundatorio(String documentoFundatorio) {
		this.documentoFundatorio = documentoFundatorio;
	}

	public String getNombreRecibo() {
		return nombreRecibo;
	}

	public void setNombreRecibo(String nombreRecibo) {
		this.nombreRecibo = nombreRecibo;
	}

	public String getRfcRecibo() {
		return rfcRecibo;
	}

	public void setRfcRecibo(String rfcRecibo) {
		this.rfcRecibo = rfcRecibo;
	}
	
	
	
}
