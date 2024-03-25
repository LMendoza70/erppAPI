package com.gisnet.erpp.service.materializacion;	

import com.gisnet.erpp.domain.PredioPersona;	

public class PredioPersonaConPorcentajes extends PredioPersona {	
    private Float porcentajeDdAnterior;	
    private Float porcentajeUvAnterior;	
    private Boolean transmite;

	public Float getPorcentajeDdAnterior() {	
		return porcentajeDdAnterior;	
	}	
	public void setPorcentajeDdAnterior(Float porcentajeDdAnterior) {	
		this.porcentajeDdAnterior = porcentajeDdAnterior;	
	}	
	public Float getPorcentajeUvAnterior() {	
		return porcentajeUvAnterior;	
	}	
	public void setPorcentajeUvAnterior(Float porcentajeUvAnterior) {	
		this.porcentajeUvAnterior = porcentajeUvAnterior;	
	}
	public Boolean getTransmite() {
		return transmite;
	}
	public void setTransmite(Boolean transmite) {
		this.transmite = transmite;
	}	




}