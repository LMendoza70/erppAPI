package com.gisnet.erpp.vo;

import java.math.BigDecimal;
import java.util.Date;

public class ConsultaPrelacionDetallePagoVO {

	private Long id;
	private Integer numRecibo;
	private String concepto;
	private String referencia;
	private String cuenta;
	private BigDecimal monto;
	private Date fechaPago;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getNumRecibo() {
		return numRecibo;
	}
	public void setNumRecibo(Integer numRecibo) {
		this.numRecibo = numRecibo;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal bigDecimal) {
		this.monto = bigDecimal;
	}
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	
}