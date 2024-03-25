package com.gisnet.erpp.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.ReciboConcepto;

public class ReciboVO {

	
    private Long id;
    private Integer noRecibo;
    private BigDecimal monto;
    private Date fecha;
	private String concepto;
    private String referencia;
    private Prelacion prelacion;
    private Acto[] actos;
    private ReciboConcepto[]  reciboConceptosParaRecibo;

    public ReciboVO() {}
    public ReciboVO(Long id,String referencia,BigDecimal monto,Date fecha) {
    	this.id =  id;
    	this.referencia =  referencia;
    	this.monto =  monto;
    	this.fecha =  fecha;
    }

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Integer getNoRecibo() {
		return noRecibo;
	}


	public void setNoRecibo(Integer noRecibo) {
		this.noRecibo = noRecibo;
	}


	public BigDecimal getMonto() {
		return monto;
	}


	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public String getReferencia() {
		return referencia;
	}


	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}


	public Prelacion getPrelacion() {
		return prelacion;
	}


	public void setPrelacion(Prelacion prelacion) {
		this.prelacion = prelacion;
	}


	public ReciboConcepto[] getReciboConceptosParaRecibo() {
		return reciboConceptosParaRecibo;
	}


	public void setReciboConceptosParaRecibo(ReciboConcepto[] conceptos) {
		this.reciboConceptosParaRecibo = conceptos;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	private String cuenta;

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}


	public Acto[] getActos() {
		return actos;
	}


	public void setActos(Acto[] actos) {
		this.actos = actos;
	}


}
