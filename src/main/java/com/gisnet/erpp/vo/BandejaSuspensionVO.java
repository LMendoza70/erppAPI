package com.gisnet.erpp.vo;

import java.util.Date;

import javax.persistence.Column;

//JADV-SUSPENSION
public class BandejaSuspensionVO {

	private Long     prelacionId;
	private String     consecutivo;
    private Date fechaIngreso;
	private Date fechaVencimiento;
	private Date fechaFirma;
    private String tipoRechazo;
    private String observaciones;
    private String acuerdo;
    private Long     diasSuspendido;
	private Boolean porAutoridad;
	private String motivo;
	private Long actoId;
	private Long noFolio;
   
   
	public String getAcuerdo() {
		return acuerdo;
	}
	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}
	public Long getDiasSuspendido() {
		return diasSuspendido;
	}
	public void setDiasSuspendido(Long diasSuspendido) {
		this.diasSuspendido = diasSuspendido;
	}
	public String getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
	public Date getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public Date getFechaFirma() {
		return fechaFirma;
	}
	public void setFechaFirma(Date fechaFirma) {
		this.fechaFirma = fechaFirma;
	}
	public String getTipoRechazo() {
		return tipoRechazo;
	}
	public void setTipoRechazo(String tipoRechazo) {
		this.tipoRechazo = tipoRechazo;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Long getPrelacionId() {
		return prelacionId;
	}
	public void setPrelacionId(Long prelacionId) {
		this.prelacionId = prelacionId;
	}
	public Boolean getPorAutoridad() {
		return porAutoridad;
	}
	public void setPorAutoridad(Boolean porAutoridad) {
		this.porAutoridad = porAutoridad;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public Long getActoId() {
		return actoId;
	}
	public void setActoId(Long actoId) {
		this.actoId = actoId;
	}
	public Long getNoFolio() {
		return noFolio;
	}
	public void setNoFolio(Long noFolio) {
		this.noFolio = noFolio;
	}
    
	
    
   
	
	
	
	
	
	
	
	
	
	
	
	
	
}
