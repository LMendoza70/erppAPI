package com.gisnet.erpp.vo.prelacion;

import java.util.Date;
import java.util.List;

public class PrelacionBoletaRegistralVO {
	private Long id;

	private Date fechaIngreso;
	private String consecutivo;
	private String oficina;
	private String observaciones;
	private String observacionesCancelacion;
	private String leyendaRegistro;
	private String inscritoEn;
	private String ubicacion;
	private String registrador;
	private String coordinador;
	private String textoRegistro;
	private String textoConsta;
	private String AFavorDe;
	private String solicitante;
	private String procedeDeFolio;

	private String numExpediente;
	private String juzgadoTribunal;
	private String tipoInforme;
	private String fechaTermino;
	private String naturalezaProcedencia;

	private String firmaRegistrador;
	private String firmaCoordinador;

	private String folioResultanteMaster;
	private String foliosFusionadosMaster;

	private List<AntecedenteModel> antecedentes;
	private List<PredioModel> predios;
	private List<ReciboModel> recibos;
	private List<TitularModel> titulares;
	private List<ActoModel> actos;
	private List<AcreedoresModel> acreedores;
	private List<ResultantesModel> resultantes;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getOficina() {
		return oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	public String getSolicitante() {
		return solicitante;
	}
	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}
	
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getInscritoEn() {
		return inscritoEn;
	}

	public void setInscritoEn(String inscritoEn) {
		this.inscritoEn = inscritoEn;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getRegistrador() {
		return registrador;
	}

	public void setRegistrador(String registrador) {
		this.registrador = registrador;
	}

	public String getCoordinador() {
		return coordinador;
	}

	public void setCoordinador(String coordinador) {
		this.coordinador = coordinador;
	}

	public String getTextoRegistro() {
		return textoRegistro;
	}

	public void setTextoRegistro(String textoRegistro) {
		this.textoRegistro = textoRegistro;
	}

	public List<AntecedenteModel> getAntecedentes() {
		return antecedentes;
	}

	public void setAntecedentes(List<AntecedenteModel> antecedentes) {
		this.antecedentes = antecedentes;
	}

	public List<PredioModel> getPredios() {
		return predios;
	}

	public void setPredios(List<PredioModel> predios) {
		this.predios = predios;
	}

	public List<ReciboModel> getRecibos() {
		return recibos;
	}

	public void setRecibos(List<ReciboModel> recibos) {
		this.recibos = recibos;
	}

	public List<TitularModel> getTitulares() {
		return titulares;
	}

	public void setTitulares(List<TitularModel> titulares) {
		this.titulares = titulares;
	}

	public List<ActoModel> getActos() {
		return actos;
	}

	public void setActos(List<ActoModel> actos) {
		this.actos = actos;
	}

	public List<AcreedoresModel> getAcreedores() {
		return acreedores;
	}

	public void setAcreedores(List<AcreedoresModel> acreedores) {
		this.acreedores = acreedores;
	}

	public String getFirmaRegistrador() {
		return firmaRegistrador;
	}

	public void setFirmaRegistrador(String firmaRegistrador) {
		this.firmaRegistrador = firmaRegistrador;
	}

	public String getFirmaCoordinador() {
		return firmaCoordinador;
	}

	public void setFirmaCoordinador(String firmaCoordinador) {
		this.firmaCoordinador = firmaCoordinador;
	}

	public String getLeyendaRegistro() {
		return leyendaRegistro;
	}

	public void setLeyendaRegistro(String leyendaRegistro) {
		this.leyendaRegistro = leyendaRegistro;
	}

	public String getTextoConsta() {
		return textoConsta;
	}

	public void setTextoConsta(String textoConsta) {
		this.textoConsta = textoConsta;
	}
	public String getAFavorDe() {
		return AFavorDe;
	}
	public void setAFavorDe(String aFavorDe) {
		AFavorDe = aFavorDe;
	}

	public void setProcedeDeFolio(String procedeDeFolio){
		this.procedeDeFolio=procedeDeFolio;
	}	

	public String getProcedeDeFolio(){
		return this.procedeDeFolio;
	}

	public void setFoliosFusionadosMaster(String foliosFusionadosMaster){
		this.foliosFusionadosMaster = foliosFusionadosMaster;
	}
	public String getFoliosFusionadosMaster(){
		return this.foliosFusionadosMaster;
	}
	public void setFolioResultanteMaster(String folioResultanteMaster){
		this.folioResultanteMaster = folioResultanteMaster;
	}

	public String getFolioResultanteMaster(){
		return this.folioResultanteMaster;
	}
	
	public String getNumExpediente() {
		return numExpediente;
	}
	
	public void setNumExpediente(String numExpediente) {
		this.numExpediente = numExpediente;
	}
	
	public String getJuzgadoTribunal() {
		return juzgadoTribunal;
	}
	
	public void setJuzgadoTribunal(String juzgadoTribunal) {
		this.juzgadoTribunal = juzgadoTribunal;
	}
	
	public String getTipoInforme() {
		return tipoInforme;
	}
	
	public void setTipoInforme(String tipoInforme) {
		this.tipoInforme = tipoInforme;
	}
	
	public String getFechaTermino() {
		return fechaTermino;
	}
	public void setFechaTermino(String fechaTermino) {
		this.fechaTermino = fechaTermino;
	}
	
	public String getNaturalezaProcedencia() {
		return naturalezaProcedencia;
	}
	
	public void setNaturalezaProcedencia(String naturalezaProcedencia) {
		this.naturalezaProcedencia = naturalezaProcedencia;
	}
	public List<ResultantesModel> getResultantes() {
		return resultantes;
	}
	public void setResultantes(List<ResultantesModel> resultantes) {
		this.resultantes = resultantes;
	}
	public String getObservacionesCancelacion() {
		return observacionesCancelacion;
	}
	public void setObservacionesCancelacion(String observacionesCancelacion) {
		this.observacionesCancelacion = observacionesCancelacion;
	}	
	
}


