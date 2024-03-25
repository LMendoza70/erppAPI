package com.gisnet.erpp.vo.caratula;

import java.util.Date;

public class TramiteVO {
	private Long consecutivo;
	private Date fecha;
	private String status;
	private String tramite;
	private Long actoId;
	private Long tipoActo;
	private String leyendaVF;
	private String documento; 
	private Date fechaActo;
	private Boolean copiadoModificado;
	private Long tipoEntrada;
	private String statusActo;
	private Long clasificacionId;
	
	public Long getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(Long consecutivo) {
		this.consecutivo = consecutivo;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTramite() {
		return tramite;
	}
	public void setTramite(String tramite) {
		this.tramite = tramite;
	}
	public Long getActoId() {
		return actoId;
	}
	public void setActoId(Long actoId) {
		this.actoId = actoId;
	}
	public Long getTipoActo() {
		return tipoActo;
	}
	public void setTipoActo(Long tipoActo) {
		this.tipoActo = tipoActo;
	}
	public String getLeyendaVF() {
		return leyendaVF;
	}
	public void setLeyendaVF(String leyendaVF) {
		this.leyendaVF = leyendaVF;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public Date getFechaActo() {
		return fechaActo;
	}
	public void setFechaActo(Date fechaActo) {
		this.fechaActo = fechaActo;
	}
	public Boolean getCopiadoModificado() {
		return copiadoModificado;
	}
	public void setCopiadoModificado(Boolean copiadoModificado) {
		this.copiadoModificado = copiadoModificado;
	}
	public Long getTipoEntrada() {
		return tipoEntrada;
	}
	public void setTipoEntrada(Long tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}
	public String getStatusActo() {
		return statusActo;
	}
	public void setStatusActo(String statusActo) {
		this.statusActo = statusActo;
	}
	public Long getClasificacionId() {
		return clasificacionId;
	}
	public void setClasificacionId(Long clasificacionId) {
		this.clasificacionId = clasificacionId;
	}

}
