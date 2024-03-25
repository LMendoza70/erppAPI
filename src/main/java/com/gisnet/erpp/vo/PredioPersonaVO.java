package com.gisnet.erpp.vo;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.Direccion;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.Identificacion;
import com.gisnet.erpp.domain.Telefono;
import com.gisnet.erpp.domain.TipoPersona;
import com.gisnet.erpp.domain.TipoRelPersona;
import com.gisnet.erpp.domain.Nacionalidad;

public class PredioPersonaVO {

	private Long id;
	private Float porcentajeDd;
	private Float porcentajeUv;
	private String hashFila = "";
	private Boolean menorEdad = false;
	private ActoPredio actoPredio ;
	private PersonaVO persona;
	private TipoRelPersona tipoRelPersona ;
	private Direccion direccion;
	private Nacionalidad nacionalidad;
	private Boolean designoBeneficiario;
	
	public TipoRelPersona getTipoRelPersona() {
		return tipoRelPersona;
	}
	public void setTipoRelPersona(TipoRelPersona tipoRelPersona) {
		this.tipoRelPersona = tipoRelPersona;
	}
	public Direccion getDireccion() {
		return direccion;
	}
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
	public Nacionalidad getNacionalidad() {
		return nacionalidad;
	}
	public void setNacionalidad(Nacionalidad nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	public Boolean getDesignoBeneficiario() {
		return designoBeneficiario;
	}
	public void setDesignoBeneficiario(Boolean designoBeneficiario) {
		this.designoBeneficiario = designoBeneficiario;
	}

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Float getPorcentajeDd() {
		return porcentajeDd;
	}
	public void setPorcentajeDd(Float porcentajeDd) {
		this.porcentajeDd = porcentajeDd;
	}
	public Float getPorcentajeUv() {
		return porcentajeUv;
	}
	public void setPorcentajeUv(Float porcentajeUv) {
		this.porcentajeUv = porcentajeUv;
	}
	public String getHashFila() {
		return hashFila;
	}
	public void setHashFila(String hashFila) {
		this.hashFila = hashFila;
	}
	public Boolean getMenorEdad() {
		return menorEdad;
	}
	public void setMenorEdad(Boolean menorEdad) {
		this.menorEdad = menorEdad;
	}
	public ActoPredio getActoPredio() {
		return actoPredio;
	}
	public void setActoPredio(ActoPredio actoPredio) {
		this.actoPredio = actoPredio;
	}
	public PersonaVO getPersona() {
		return persona;
	}
	public void setPersona(PersonaVO persona) {
		this.persona = persona;
	}
	
	public PredioPersona transformIntoPredioPersona() {
    	
    	PredioPersona predioPersona = new PredioPersona();
    	predioPersona.setId(this.getId());
    	predioPersona.setPorcentajeDd(this.getPorcentajeDd());
    	predioPersona.setPorcentajeUv(this.getPorcentajeUv());
    	predioPersona.setHashFila(this.getHashFila());
    	predioPersona.setActoPredio(this.getActoPredio());
    	predioPersona.setPersona(this.getPersona().transformIntoPersona());
    	predioPersona.setTipoRelPersona(this.getTipoRelPersona());
    	predioPersona.setDireccion(this.getDireccion());
    	predioPersona.setNacionalidad(this.getNacionalidad());
    	predioPersona.setDesignoBeneficiario(this.getDesignoBeneficiario());
    	
    	return predioPersona;
    }

	
}