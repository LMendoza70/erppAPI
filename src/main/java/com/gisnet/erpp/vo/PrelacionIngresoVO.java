package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.Recibo;
import com.gisnet.erpp.vo.RequisitoVO;
import com.gisnet.erpp.vo.PredioVO;
import com.gisnet.erpp.vo.AntecedenteVO;

import java.util.Date;
import java.util.List;

public class PrelacionIngresoVO {
	private Long id;

	/*private Usuario solicitante;
	private Documento[] documentos;
	private RequisitoVO[]  requisitos;
	private Recibo[] recibos;
	private AntecedenteVO[] antecedentes;
	private PredioVO[] predios;
	private Boolean presencial;
	*/
	private Date fechaIngreso;
	private String consecutivo;
	private String tiposActo;
	private String actos;
	private String oficina;
	private String datosDocumentoFundatorio;
	private List<ReciboVO> recibos;
	private List<FolioVO> folios;
	private String observaciones;
	private String nombreSolicita;
	private String ubicacion;
	private String comodin;
	private String prioridad;
	private Date fechaReingreso;
	private String clave;
	
	public String getNombreRegistrador() {
		return nombreRegistrador;
	}

	public void setNombreRegistrador(String nombreRegistrador) {
		this.nombreRegistrador = nombreRegistrador;
	}

	private String nombreRegistrador;
	private String emailSolicita;
	private String tipoRecepcion;
	private String tipoEntrega;
	
	




	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	/*public Acto[] getActos() {
		return actos;
	}
	public void setActos(Acto[] actos) {
		this.actos = actos;
	}
	public Usuario getSolicitante() {
		return solicitante;
	}
	public void setSolicitante(Usuario solicitante) {
		this.solicitante = solicitante;
	}
	public Documento[] getDocumentos() {
		return documentos;
	}
	public void setDocumentos(Documento[] documentos) {
		this.documentos = documentos;
	}
	public RequisitoVO[] getRequisitos() {
		return requisitos;
	}
	public void setRequisitos(RequisitoVO[] requisitos) {
		this.requisitos = requisitos;
	}
	public Recibo[] getRecibos() {
		return recibos;
	}
	public void setRecibos(Recibo[] recibos) {
		this.recibos = recibos;
	}
	public AntecedenteVO[] getAntecedentes() {
		return antecedentes;
	}
	public void setAntecedentes(AntecedenteVO[] antecedentes) {
		this.antecedentes = antecedentes;
	}
	public PredioVO[] getPredios() {
		return predios;
	}
	public void setPredios(PredioVO[] predios) {
		this.predios = predios;
	} 
	*/
	
	/*public Boolean isPresencial(){

		return this.presencial;
	}

	public void setPresencial(Boolean presencial){
		this.presencial=presencial;

	}

	//////

	public Boolean getPresencial() {
		return presencial;
	}
*/
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

	public String getTiposActo() {
		return tiposActo;
	}

	public void setTiposActo(String tiposActo) {
		this.tiposActo = tiposActo;
	}

	public String getActos() {
		return this.actos;
	}

	public void setActos(String actos) {
		this.actos = actos;
	}

	public String getOficina() {
		return oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	public String getDatosDocumentoFundatorio() {
		return datosDocumentoFundatorio;
	}

	public void setDatosDocumentoFundatorio(String datosDocumentoFundatorio) {
		this.datosDocumentoFundatorio = datosDocumentoFundatorio;
	}

	public void setRecibos(List<ReciboVO> recibos) {
		this.recibos = recibos;
	}

	public List<ReciboVO> getRecibos () {
		return this.recibos;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getNombreSolicita() {
		return nombreSolicita;
	}

	public void setNombreSolicita(String nombreSolicita) {
		this.nombreSolicita = nombreSolicita;
	}

	public String getEmailSolicita() {
		return emailSolicita;
	}

	public void setEmailSolicita(String emailSolicita) {
		this.emailSolicita = emailSolicita;
	}

	public String getTipoRecepcion() {
		return tipoRecepcion;
	}

	public void setTipoRecepcion(String tipoRecepcion) {
		this.tipoRecepcion = tipoRecepcion;
	}

	public String getTipoEntrega() {
		return tipoEntrega;
	}

	public void setTipoEntrega(String tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
	}
	public String getUbicacion() {
		return this.ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public String getComodin() {
		return this.comodin;
	}

	public void setComodin(String comodin) {
		this.comodin = comodin;
	}

	public List<FolioVO> getFolios() {
		return folios;
	}

	public void setFolios(List<FolioVO> folios) {
		this.folios = folios;
	}

	public String getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}

	public Date getFechaReingreso() {
		return fechaReingreso;
	}

	public void setFechaReingreso(Date fechaReingreso) {
		this.fechaReingreso = fechaReingreso;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

}
