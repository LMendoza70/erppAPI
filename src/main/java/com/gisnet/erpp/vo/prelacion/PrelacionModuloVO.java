package com.gisnet.erpp.vo.prelacion;

public class PrelacionModuloVO {
	private Long id;
	private String consecutivo;
	private String anio;

	public PrelacionModuloVO() {
		
	}
	public PrelacionModuloVO(Long id,String consecutivo,String anio) {
		this.id  = id;
		this.consecutivo = consecutivo;
		this.anio =  anio;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

}
