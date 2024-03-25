package com.gisnet.erpp.vo.caratula;

import java.util.Date;
import java.util.List;

public class MuebleVO {
	private AntecedenteVO antecedente;
	
	private Long muebleId;
	
	private boolean primerRegistro;
	private Integer noFolio;
	
	private String objeto;
	private String especificacion;
	private String marca;
	private String modelo;
	private String numSerie;
	private String numDocIdentif;
	private String numExt;
	private Date fechaDocIdentif;
	
	private Boolean bloqueado = new Boolean(false);

	private List<ActoVO> sumatoriaActosPorClasifActo;
	
	private List<TramiteVO> tramites;
	
	private boolean enProceso;

	public AntecedenteVO getAntecedente() {
		return antecedente;
	}

	public void setAntecedente(AntecedenteVO antecedente) {
		this.antecedente = antecedente;
	}
	
	public String getObjeto() {
		return objeto;
	}

	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}

	public String getEspecificacion() {
		return especificacion;
	}

	public void setEspecificacion(String especificacion) {
		this.especificacion = especificacion;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getNumSerie() {
		return numSerie;
	}

	public void setNumSerie(String numSerie) {
		this.numSerie = numSerie;
	}

	public String getNumDocIdentif() {
		return numDocIdentif;
	}

	public void setNumDocIdentif(String numDocIdentif) {
		this.numDocIdentif = numDocIdentif;
	}

	public String getNumExt() {
		return numExt;
	}

	public void setNumExt(String numExt) {
		this.numExt = numExt;
	}

	public Date getFechaDocIdentif() {
		return fechaDocIdentif;
	}

	public void setFechaDocIdentif(Date fechaDocIdentif) {
		this.fechaDocIdentif = fechaDocIdentif;
	}

	public List<ActoVO> getSumatoriaActosPorClasifActo() {
		return sumatoriaActosPorClasifActo;
	}

	public void setSumatoriaActosPorClasifActo(List<ActoVO> sumatoriaActosPorClasifActo) {
		this.sumatoriaActosPorClasifActo = sumatoriaActosPorClasifActo;
	}

	public List<TramiteVO> getTramites() {
		return tramites;
	}

	public void setTramites(List<TramiteVO> tramites) {
		this.tramites = tramites;
	}

	public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public boolean isPrimerRegistro() {
		return primerRegistro;
	}

	public void setPrimerRegistro(boolean primerRegistro) {
		this.primerRegistro = primerRegistro;
	}

	public Integer getNoFolio() {
		return noFolio;
	}

	public void setNoFolio(Integer noFolio) {
		this.noFolio = noFolio;
	}

	public Long getMuebleId() {
		return muebleId;
	}

	public void setMuebleId(Long muebleId) {
		this.muebleId = muebleId;
	}

	public boolean isEnProceso() {
		return enProceso;
	}

	public void setEnProceso(boolean enProceso) {
		this.enProceso = enProceso;
	}

}
