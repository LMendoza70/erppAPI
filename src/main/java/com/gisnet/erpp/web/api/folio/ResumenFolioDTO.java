package com.gisnet.erpp.web.api.folio;

public class ResumenFolioDTO {
	private Long folioId;
	private Long oficinaId;
	private String oficinaNombre;
	private Long tipoFolioId;
	private String tipoFolioNombre;
	private Integer noFolio;
	private String descripcion;
	private Boolean caratulaActualizada;
	private Boolean bloqueado;
	
	public ResumenFolioDTO() {}
	
	public ResumenFolioDTO(Long folioId, Long oficinaId, String oficinaNombre, Long tipoFolioId, String tipoFolioNombre, Integer noFolio, String descripcion, Boolean caratulaActualizada, Boolean bloqueado) {
		super();
		this.folioId = folioId;
		this.oficinaId = oficinaId;
		this.oficinaNombre = oficinaNombre;
		this.tipoFolioId = tipoFolioId;
		this.tipoFolioNombre = tipoFolioNombre;
		this.noFolio = noFolio;
		this.descripcion = descripcion;
		this.bloqueado = bloqueado;
	}
	
	
	public Long getFolioId() {
		return folioId;
	}
	public void setFolioId(Long folioId) {
		this.folioId = folioId;
	}
	public Long getOficinaId() {
		return oficinaId;
	}
	public void setOficinaId(Long oficinaId) {
		this.oficinaId = oficinaId;
	}
	public String getOficinaNombre() {
		return oficinaNombre;
	}
	public void setOficinaNombre(String oficinaNombre) {
		this.oficinaNombre = oficinaNombre;
	}
	public Long getTipoFolioId() {
		return tipoFolioId;
	}
	public void setTipoFolioId(Long tipoFolioId) {
		this.tipoFolioId = tipoFolioId;
	}
	public String getTipoFolioNombre() {
		return tipoFolioNombre;
	}
	public void setTipoFolioNombre(String tipoFolioNombre) {
		this.tipoFolioNombre = tipoFolioNombre;
	}
	public Integer getNoFolio() {
		return noFolio;
	}
	public void setNoFolio(Integer noFolio) {
		this.noFolio = noFolio;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Boolean isBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	public Boolean getCaratulaActualizada() {
		return caratulaActualizada;
	}

	public void setCaratulaActualizada(Boolean caratulaActualizada) {
		this.caratulaActualizada = caratulaActualizada;
	}	
	
	
	
}
