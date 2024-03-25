package com.gisnet.erpp.vo.caratula;

import com.gisnet.erpp.domain.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

public class PropietarioVO implements Serializable {
	private TipoPersona tipoPersona;
	private String nombre;
	private String paterno;
	private String materno;
	private Date fechaNac;
	private String rfc;
	private String curp;
	private String estadoCivil;
	private String regimen;
	private String nacionalidad;
	private boolean designoBeneficiario;
	private Float porcentajeDd;
	private Float porcentajeUv;
	
	public TipoPersona getTipoPersona() {
		return tipoPersona;
	}
	public void setTipoPersona(TipoPersona tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
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
	public boolean isDesignoBeneficiario() {
		return designoBeneficiario;
	}
	public void setDesignoBeneficiario(boolean designoBeneficiario) {
		this.designoBeneficiario = designoBeneficiario;
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
	
	public static PropietarioVO converToPropietarioVO(@NotNull PredioPersona pp) {
		Persona persona = pp.getPersona();

		PropietarioVO pvo = new PropietarioVO();

		pvo.setTipoPersona(persona.getTipoPersona());
		pvo.curp = persona.getCurp();
		pvo.nombre = persona.getNombre();
		pvo.paterno = persona.getPaterno();
		pvo.materno = persona.getMaterno();
		pvo.fechaNac = persona.getFechaNac();
		pvo.rfc = persona.getRfc();
		//final String nombre = pp.getEstadoCivil().getNombre();
        pvo.estadoCivil = Optional.ofNullable( pp.getEstadoCivil()).map(EstadoCivil::getNombre).orElse("");
		pvo.regimen = Optional.ofNullable( pp.getRegimen()).map(Regimen::getNombre).orElse("");
		pvo.nacionalidad = Optional.ofNullable( pp.getNacionalidad()).map(Nacionalidad::getNombre).orElse("");
		pvo.designoBeneficiario = pp.getPredioPropDesignan() != null ? true : false;
		pvo.porcentajeDd = pp.getPorcentajeDd();
		pvo.porcentajeUv = pp.getPorcentajeUv();

		return pvo;
	}

}
