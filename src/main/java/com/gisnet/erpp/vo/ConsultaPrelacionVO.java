package com.gisnet.erpp.vo;


import java.util.Date;
import java.util.List;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.vo.*;

public class ConsultaPrelacionVO {

	private Long id;
	private Prioridad prioridad;
	private List<Acto> actos;
	private Integer consecutivo;
	private Date fechaRegistro;
	private Date fechaEnvioFirma;
	private Date fechaBaja;
	
	private List <Documento > escrituras;
	private List<Notario> notarios;
	private Usuario registrador;
	private Usuario usuarioAnalista;
	
	private Status status;
	
	
	
	private ServicioAndSubVO[] servicios;
	private Usuario solicitante;
	private RequisitoVO[]  requisitos;
	private ReciboVO[] recibos;
	private Antecedente[] antecedentes;
	private PredioVO[] predios;

	private PrelacionAnte[] prelacionAntes;
	private PrelacionPredio[] prelacionesPredio;

	private PersonaJuridica[] personasJuridicas;

	private FolioSeccionAuxiliar[] folioSeccionAuxiliares;

	private Mueble[] muebles;

	private Boolean presencial;
	private String  observaciones;
	private Boolean tipoEntrega;

	private String tipoBusqueda;
	private Boolean tieneTermino;
	private Integer dias;
	private Integer horas;
	private Boolean contienePago;
	private Identificacion identificacionPersona;

	private Municipio municipio;
	private Usuario usuarioCoordinador;
	private String numerosRecibos;
	private String tipoFolio;
	private Long id_entrada;
	
	private String contratantes;
	
	private Boolean esDigitalizado;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Acto> getActos() {
		return actos;
	}
	public void setActos(List<Acto> actos) {
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
	
	
	
	
	
	public Prioridad getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(Prioridad prioridad) {
		this.prioridad = prioridad;
	}
	public Integer getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public Date getFechaEnvioFirma() {
		return fechaEnvioFirma;
	}
	public void setFechaEnvioFirma(Date fechaEnvioFirma) {
		this.fechaEnvioFirma = fechaEnvioFirma;
	}
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	public List <Documento > getEscrituras() {
		return escrituras;
	}
	public void setEscrituras(List <Documento > escrituras) {
		this.escrituras = escrituras;
	}
	public List<Notario> getNotarios() {
		return notarios;
	}
	public void setNotarios(List<Notario> notarios) {
		this.notarios = notarios;
	}
	public Usuario getRegistrador() {
		return registrador;
	}
	public void setRegistrador(Usuario registrador) {
		this.registrador = registrador;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * @return the usuarioAnalista
	 */
	public Usuario getUsuarioAnalista() {
		return usuarioAnalista;
	}
	/**
	 * @param usuarioAnalista the usuarioAnalista to set
	 */
	public void setUsuarioAnalista(Usuario usuarioAnalista) {
		this.usuarioAnalista = usuarioAnalista;
	}
	public Municipio getMunicipio() {
		return municipio;
	}
	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	public Usuario getUsuarioCoordinador() {
		return usuarioCoordinador;
	}
	public void setUsuarioCoordinador(Usuario usuarioCoordinador) {
		this.usuarioCoordinador = usuarioCoordinador;
	}
	public String getNumerosRecibos() {
		return numerosRecibos;
	}
	public void setNumerosRecibos(String numerosRecibos) {
		this.numerosRecibos = numerosRecibos;
	}
	public String getTipoFolio() {
		return tipoFolio;
	}
	public void setTipoFolio(String tipoFolio) {
		this.tipoFolio = tipoFolio;
	}
	public String getContratantes() {
		return contratantes;
	}
	public void setContratantes(String contratantes) {
		this.contratantes = contratantes;
	}
	public Long getId_entrada() {
		return id_entrada;
	}
	public void setId_entrada(Long id_entrada) {
		this.id_entrada = id_entrada;
	}
	
	public Boolean getEsDigitalizado() {
		return esDigitalizado;
	}
	public void setEsDigitalizado(Boolean esDigitalizado) {
		this.esDigitalizado = esDigitalizado;
	}
	
	

}
