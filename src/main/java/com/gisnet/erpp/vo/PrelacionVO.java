package com.gisnet.erpp.vo;


import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.vo.*;

public class PrelacionVO {

	private Long id;
	private Acto[] actos;
	private ServicioAndSubVO[] servicios;
	private Usuario solicitante;
	private RequisitoVO[]  requisitos;
	private ReciboVO[] recibos;
	private Antecedente[] antecedentes;
	private PredioVO[] predios;
	private boolean urgente;

	private PrelacionAnte[] prelacionAntes;
	private PrelacionPredio[] prelacionesPredio;

	private PersonaJuridica[] personasJuridicas;

	private FolioSeccionAuxiliar[] folioSeccionAuxiliares;

	private Mueble[] muebles;

	private Boolean presencial;
	private Observacion observacion;
	private String  observaciones;
	private Boolean tipoEntrega;

	private String tipoBusqueda;
	private Boolean tieneTermino;
	private Integer dias;
	private Integer horas;
	private Boolean contienePago;
	private Identificacion identificacionPersona;

	private TipoAclaracion tipoAclaracion;

	private Boolean primerRegistro;

	private Area area;

	private Boolean isExcentoPago;

	private Boolean esPresencial;

	private Oficina oficina;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Acto[] getActos() {
		return actos;
	}
	public void setActos(Acto[] actos) {
		this.actos = actos;
	}
	public ServicioAndSubVO[] getServicios() {
		return servicios;
	}
	public void setServicios(ServicioAndSubVO[] servicios) {
		this.servicios = servicios;
	}
	public Usuario getSolicitante() {
		return solicitante;
	}
	public void setSolicitante(Usuario solicitante) {
		this.solicitante = solicitante;
	}
	public RequisitoVO[] getRequisitos() {
		return requisitos;
	}
	public void setRequisitos(RequisitoVO[] requisitos) {
		this.requisitos = requisitos;
	}
	public ReciboVO[] getRecibos() {
		return recibos;
	}
	public void setRecibos(ReciboVO[] recibos) {
		this.recibos = recibos;
	}
	public Antecedente[] getAntecedentes() {
		return antecedentes;
	}
	public void setAntecedentes(Antecedente[] antecedentes) {
		this.antecedentes = antecedentes;
	}
	public PredioVO[] getPredios() {
		return predios;
	}
	public void setPredios(PredioVO[] predios) {
		this.predios = predios;
	}
	public Boolean getPresencial() {
		return presencial;
	}
	public void setPresencial(Boolean presencial) {
		this.presencial = presencial;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Boolean getTipoEntrega() {
		return tipoEntrega;
	}
	public void setTipoEntrega(Boolean tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
	}
	public String getTipoBusqueda() {
		return tipoBusqueda;
	}
	public void setTipoBusqueda(String tipoBusuqeda) {
		this.tipoBusqueda = tipoBusuqeda;
	}
	public PersonaJuridica[] getPersonasJuridicas() {
		return personasJuridicas;
	}
	public void setPersonasJuridicas(PersonaJuridica[] personasJuridicas) {
		this.personasJuridicas = personasJuridicas;
	}
	public FolioSeccionAuxiliar[] getFolioSeccionAuxiliares() {
		return folioSeccionAuxiliares;
	}
	public void setFolioSeccionAuxiliares(FolioSeccionAuxiliar[] folioSeccionAuxiliares) {
		this.folioSeccionAuxiliares = folioSeccionAuxiliares;
	}
	public Mueble[] getMuebles() {
		return muebles;
	}
	public void setMuebles(Mueble[] muebles) {
		this.muebles = muebles;
	}
	public PrelacionPredio[] getPrelacionesPredio() {
		return this.prelacionesPredio;
	}
	public void setPrelacionesPredio(PrelacionPredio[] prelacionesPredio) {
		this.prelacionesPredio = prelacionesPredio;
	}

	public PrelacionAnte[] getPrelacionAntes() {
		return prelacionAntes;
	}

	public void setPrelacionAntes(PrelacionAnte[] prelacionAntes) {
		this.prelacionAntes = prelacionAntes;
	}


	public Boolean getTieneTermino(){
		return this.tieneTermino;
	}

	public void setTieneTermino(Boolean tieneTermino){
		this.tieneTermino=tieneTermino;
	}

	public Boolean getUrgente(){
		return this.urgente;
	}

	public void setUrgente(Boolean urgente){
		this.urgente=urgente;
	}

	public Integer getDias(){
		return this.dias;
	}

	public void setDias(Integer dias){
		this.dias=dias;
	}

	public Integer getHoras(){
		return this.horas;
	}

	public void setHoras(Integer horas){
		this.horas=horas;
	}


	public Identificacion getIdentificacionPersona() {
		return identificacionPersona;
	}

	public void setIdentificacionPersona(Identificacion identificacionPersona) {
		this.identificacionPersona = identificacionPersona;
	}

	public Boolean getContienePago() {
		return this.contienePago;
	}
	public void setContienePago(Boolean contienePago) {
		this.contienePago = contienePago;
	}

	public Observacion getObservacion() {
		return observacion;
	}

	public void setObservacion(Observacion observacion) {
		this.observacion = observacion;
	}
	/**
	 * @return the tipoAclaracion
	 */
	public TipoAclaracion getTipoAclaracion() {
		return tipoAclaracion;
	}
	/**
	 * @param tipoAclaracion the tipoAclaracion to set
	 */
	public void setTipoAclaracion(TipoAclaracion tipoAclaracion) {
		this.tipoAclaracion = tipoAclaracion;
	}
	public Boolean getPrimerRegistro() {
		return primerRegistro;
	}
	public void setPrimerRegistro(Boolean primerRegistro) {
		this.primerRegistro = primerRegistro;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	public Boolean getIsExcentoPago() {
		return isExcentoPago;
	}
	public void setIsExcentoPago(Boolean isExcentoPago) {
		this.isExcentoPago = isExcentoPago;
	}

	public Boolean getEsPresencial() {
		return esPresencial;
	}
	public void setEsPresencial(Boolean esPresencial) {
		this.esPresencial = esPresencial;
	}
	public Oficina getOficina() {
		return oficina;
	}
	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}


}
