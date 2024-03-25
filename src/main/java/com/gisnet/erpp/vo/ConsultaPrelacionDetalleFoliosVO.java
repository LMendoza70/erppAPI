package com.gisnet.erpp.vo;

public class ConsultaPrelacionDetalleFoliosVO {

	private Long id;
	private String folios;
	private String documentoFundatorio;
	private String origenDocumentoFundatorio;
	private String actoServicioSolicitado;
	private String estatus;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFolios() {
		return folios;
	}
	public void setFolios(String folios) {
		this.folios = folios;
	}
	public String getDocumentoFundatorio() {
		return documentoFundatorio;
	}
	public void setDocumentoFundatorio(String documentoFundatorio) {
		this.documentoFundatorio = documentoFundatorio;
	}
	public String getOrigenDocumentoFundatorio() {
		return origenDocumentoFundatorio;
	}
	public void setOrigenDocumentoFundatorio(String origenDocumentoFundatorio) {
		this.origenDocumentoFundatorio = origenDocumentoFundatorio;
	}
	public String getActoServicioSolicitado() {
		return actoServicioSolicitado;
	}
	public void setActoServicioSolicitado(String actoServicioSolicitado) {
		this.actoServicioSolicitado = actoServicioSolicitado;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
}
