package com.gisnet.erpp.vo.fichaRegistral;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.gisnet.erpp.domain.EstadoCivil;
import com.gisnet.erpp.domain.Nacionalidad;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.Regimen;

public class TitularesVO implements Serializable {
	private String 	nombre;
	private String 	paterno;
	private String 	materno;
	private Date 	fechaNac;
	private String 	rfc;
	private String 	curp;
	private String 	estadoCivil;
	private String 	regimen;
	private String 	nacionalidad;
	private Float 	porcentajeDd;
	private Float 	porcentajeUv;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPaterno() {
		return paterno;
	}
	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}
	public String getMaterno() {
		return materno;
	}
	public void setMaterno(String materno) {
		this.materno = materno;
	}
	public Date getFechaNac() {
		return fechaNac;
	}
	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getCurp() {
		return curp;
	}
	public void setCurp(String curp) {
		this.curp = curp;
	}
	public String getEstadoCivil() {
		return estadoCivil;
	}
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}
	public String getRegimen() {
		return regimen;
	}
	public void setRegimen(String regimen) {
		this.regimen = regimen;
	}
	public String getNacionalidad() {
		return nacionalidad;
	}
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
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
	
	public static TitularesVO converToTitularesVO(@NotNull PredioPersona pp) {
		Persona persona = pp.getPersona();

		TitularesVO tvo = new TitularesVO();

		tvo.nombre = persona.getNombre();
		tvo.paterno = persona.getPaterno();
		tvo.materno = persona.getMaterno();
		tvo.fechaNac = persona.getFechaNac();
		tvo.rfc = persona.getRfc();
		tvo.curp = persona.getCurp();
		tvo.estadoCivil = Optional.ofNullable( pp.getEstadoCivil()).map(EstadoCivil::getNombre).orElse("");
		tvo.regimen = Optional.ofNullable( pp.getRegimen()).map(Regimen::getNombre).orElse("");
		tvo.nacionalidad = Optional.ofNullable( pp.getNacionalidad()).map(Nacionalidad::getNombre).orElse("");
		tvo.porcentajeDd = pp.getPorcentajeDd();
		tvo.porcentajeUv = pp.getPorcentajeUv();

		return tvo;
	}

}
