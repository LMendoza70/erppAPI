package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.Prelacion;

public class PrelacionCopiaCertificadaFolioVO {

	private Prelacion prelacion;
	
	private String nombreTipoActo;
	
	private  Long actoId;
	
	private Boolean seleccionado;

	public Prelacion getPrelacion() {
		return prelacion;
	}

	public void setPrelacion(Prelacion prelacion) {
		this.prelacion = prelacion;
	}

	public String getNombreTipoActo() {
		return nombreTipoActo;
	}

	public void setNombreTipoActo(String nombreTipoActo) {
		this.nombreTipoActo = nombreTipoActo;
	}

	public Long getActoId() {
		return actoId;
	}

	public void setActoId(Long actoId) {
		this.actoId = actoId;
	}

	public Boolean getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(Boolean seleccionado) {
		this.seleccionado = seleccionado;
	}
		
}