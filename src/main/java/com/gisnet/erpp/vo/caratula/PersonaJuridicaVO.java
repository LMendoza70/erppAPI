package com.gisnet.erpp.vo.caratula;

import java.util.Date;
import java.util.List;

public class PersonaJuridicaVO {
	private AntecedenteVO antecedente;
	
	private List<AntecedenteVO> antecedentes;
	
	private Long personaJuridicaId;

	private boolean primerRegistro;
	private Integer noFolio;
	
	private String tipoSociedad;
	private String denominacionNombre;
	
	private String duracion;
	
	private String objeto;
	

	private String municipio;
	private String estado;
	
	private int numApoderados;
	private int numIntegrantes;
	private int numSocios;
	
	private Boolean bloqueado = new Boolean(false);
	
	private boolean libre;
	
	private String direccion;
	
	private List<ActoVO> sumatoriaActosPorClasifActo;
	
	private List<TramiteVO> tramites;
	
	private List<DevArt71VO> devoluciones;
	
	private boolean enProceso;
	
	private Date fechaApertura;
	
	private Integer numeroFolioReal;

	public AntecedenteVO getAntecedente() {
		return antecedente;
	}

	public void setAntecedente(AntecedenteVO antecedente) {
		this.antecedente = antecedente;
	}

	public String getTipoSociedad() {
		return tipoSociedad;
	}

	public void setTipoSociedad(String tipoSociedad) {
		this.tipoSociedad = tipoSociedad;
	}

	public String getDenominacionNombre() {
		return denominacionNombre;
	}

	public void setDenominacionNombre(String denominacionNombre) {
		this.denominacionNombre = denominacionNombre;
	}

	public String getDuracion() {
		return duracion;
	}

	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	public String getObjeto() {
		return objeto;
	}

	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getNumApoderados() {
		return numApoderados;
	}

	public void setNumApoderados(int numApoderados) {
		this.numApoderados = numApoderados;
	}

	public int getNumIntegrantes() {
		return numIntegrantes;
	}

	public void setNumIntegrantes(int numIntegrantes) {
		this.numIntegrantes = numIntegrantes;
	}

	public int getNumSocios() {
		return numSocios;
	}

	public void setNumSocios(int numSocios) {
		this.numSocios = numSocios;
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

	public boolean isLibre() {
		return libre;
	}

	public void setLibre(boolean libre) {
		this.libre = libre;
	}

	public List<DevArt71VO> getDevoluciones() {
		return devoluciones;
	}

	public void setDevoluciones(List<DevArt71VO> devoluciones) {
		this.devoluciones = devoluciones;
	}

	public Long getPersonaJuridicaId() {
		return personaJuridicaId;
	}

	public void setPersonaJuridicaId(Long personaJuridicaId) {
		this.personaJuridicaId = personaJuridicaId;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public boolean isEnProceso() {
		return enProceso;
	}

	public void setEnProceso(boolean enProceso) {
		this.enProceso = enProceso;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public Integer getNumeroFolioReal() {
		return numeroFolioReal;
	}

	public void setNumeroFolioReal(Integer numeroFolioReal) {
		this.numeroFolioReal = numeroFolioReal;
	}

	public List<AntecedenteVO> getAntecedentes() {
		return antecedentes;
	}

	public void setAntecedentes(List<AntecedenteVO> antecedentes) {
		this.antecedentes = antecedentes;
	}

}
