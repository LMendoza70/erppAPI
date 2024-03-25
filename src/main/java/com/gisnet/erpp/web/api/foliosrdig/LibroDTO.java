package com.gisnet.erpp.web.api.foliosrdig;

import com.gisnet.erpp.domain.Oficina;

public class LibroDTO {
	private Long libroId;
	private String documento;
	private String documentoBis;
	private int paginaIni;
	private int paginaFin;
	private String oficina;

	public int getPaginaIni() {
		return paginaIni;
	}
	public void setPaginaIni(int paginaIni) {
		this.paginaIni = paginaIni;
	}
	public int getPaginaFin() {
		return paginaFin;
	}
	public void setPaginaFin(int paginaFin) {
		this.paginaFin = paginaFin;
	}
	public Long getLibroId() {
		return libroId;
	}
	public void setLibroId(Long libroId) {
		this.libroId = libroId;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getDocumentoBis() {
		return documentoBis;
	}
	public void setDocumentoBis(String documentoBis) {
		this.documentoBis = documentoBis;
	}
	public String getOficina() {
		return oficina;
	}
	public void setOficina(String oficina) {
		this.oficina = oficina;
	}


	
}
