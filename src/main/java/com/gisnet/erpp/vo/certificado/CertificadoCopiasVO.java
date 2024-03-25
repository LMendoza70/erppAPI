package com.gisnet.erpp.vo.certificado;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.gisnet.erpp.vo.GravamenCountVO;
import com.gisnet.erpp.vo.LimitacionCountVO;

public class CertificadoCopiasVO implements Serializable {

	private Long prelacionId;
	private int consecutivo;
	private Date fechaIngreso;
	private String numHojas;
	private String escritura;
	private String lugar;
	private Integer predio;
	private Date fechaRegistro;
	private Date fechaGenerado;
	private String certifica;

	private Integer recibo;
	private BigDecimal monto;
	private String conLetra;
	private String concepto;
	private Integer folio;
	private Integer libro;

	private Long usuarioSolicitanteId;
	private String solicitante;

	private Long usuarioAutorizoId;
	private String usuarioAutorizo;
	private String firmaAutorizo;

	private String pkcs7;
	private Integer secuencia;
	private String politica;
	private String digestion;
	private Integer secuenciaTS;
	private Date estampilla;

	private String director;
	private String firma;

	private String registrador;
	private String coordinador;
	private String firmaRegistrador;
	private String firmaCoordinador;
	private String oficina;

	public Integer getFolio() {
		return folio;
	}

	/**
	 * @return the certifica
	 */
	public String getCertifica() {
		return certifica;
	}

	/**
	 * @param certifica the certifica to set
	 */
	public void setCertifica(String certifica) {
		this.certifica = certifica;
	}

	public void setFolio(Integer folio) {
		this.folio = folio;
	}

	public Integer getLibro() {
		return libro;
	}

	public void setLibro(Integer libro) {
		this.libro = libro;
	}

	public String getEscritura() {
		return escritura;
	}

	public void setEscritura(String escritura) {
		this.escritura = escritura;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public String getConLetra() {
		return conLetra;
	}

	public void setConLetra(String conLetra) {
		this.conLetra = conLetra;
	}
	
	public String getFirmaAutorizo() {
		return firmaAutorizo;
	}

	public void setFirmaAutorizo(String firmaAutorizo) {
		this.firmaAutorizo = firmaAutorizo;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public int getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}
	
	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}
	
	public Date getFechaGenerado() {
		return fechaGenerado;
	}

	public void setFechaGenerado(Date fechaGenerado) {
		this.fechaGenerado = fechaGenerado;
	}

	public Long getPrelacionId() {
		return prelacionId;
	}

	public void setPrelacionId(Long prelacionId) {
		this.prelacionId = prelacionId;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getNumHojas() {
		return numHojas;
	}

	public void setNumHojas(String numHojas) {
		this.numHojas = numHojas;
	}

	public Integer getPredio() {
		return predio;
	}

	public void setPredioId(Integer String) {
		this.predio = predio;
	}

	public Integer getRecibo() {
		return recibo;
	}

	public void setRecibo(Integer recibo) {
		this.recibo = recibo;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public Long getUsuarioSolicitanteId() {
		return usuarioSolicitanteId;
	}

	public void setUsuarioSolicitanteId(Long usuarioSolicitanteId) {
		this.usuarioSolicitanteId = usuarioSolicitanteId;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}
	
	public Long getUsuarioAutorizoId() {
		return usuarioAutorizoId;
	}

	public void setUsuarioAutorizoId(Long usuarioAutorizoId) {
		this.usuarioAutorizoId = usuarioAutorizoId;
	}

	public String getUsuarioAutorizo() {
		return usuarioAutorizo;
	}

	public void setUsuarioAutorizo(String usuarioAutorizo) {
		this.usuarioAutorizo = usuarioAutorizo;
	}

	public String getPkcs7() {
		return pkcs7;
	}

	public void setPkcs7(String pkcs7) {
		this.pkcs7 = pkcs7;
	}

	public Integer getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(Integer secuencia) {
		this.secuencia = secuencia;
	}

	public String getPolitica() {
		return politica;
	}

	public void setPolitica(String politica) {
		this.politica = politica;
	}

	public String getDigestion() {
		return digestion;
	}

	public void setDigestion(String digestion) {
		this.digestion = digestion;
	}

	public Integer getSecuenciaTS() {
		return secuenciaTS;
	}

	public void setSecuenciaTS(Integer secuenciaTS) {
		this.secuenciaTS = secuenciaTS;
	}

	public Date getEstampilla() {
		return estampilla;
	}

	public void setEstampilla(Date estampilla) {
		this.estampilla = estampilla;
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

	public String toString() {

		return "Certificado :  = ID " + getPrelacionId() + " -Consecutivo=  " + getConsecutivo() + " - FechaIngreso = "
				+ getFechaIngreso() + " - NumHojas= " + getNumHojas() + " -  Escritura= " + getEscritura()
				+ " -  Lugar = " + getLugar() + " - Predio= " + getPredio() + " -Lugar " + getLugar()
				+ " - FechaRegistro=  " + getFechaRegistro() + " - Fecha generado" + getFechaGenerado() + " - Recibo=  "
				+ getRecibo() + "- Montos=" + getMonto() + " - LetraP= " + getConLetra() + "- Concepto " + getConcepto()
				+ "  Libro= " + libro + " Folio " + folio + " solicitante " + usuarioSolicitanteId + "  - Solicitante= "
				+ solicitante + " Usuario Elaboro= " + registrador + " usuario autorizo = " + coordinador
				+ " firmaElaboracion= " + firmaRegistrador + " firmaAutorizacion = " + firmaCoordinador ;

	}

	public String getOficina() {
		return oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}
	
}
