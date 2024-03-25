package com.gisnet.erpp.vo.certificado;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.gisnet.erpp.vo.GravamenCountVO;
import com.gisnet.erpp.vo.LimitacionCountVO;
import com.gisnet.erpp.vo.caratula.ActoVO;

public class CertificadoLibertadOGravamenVO implements Serializable{

	private Long prelacionId;
	private Long Id;
	private int consecutivo;
	private Date fechaIngreso;
    private String lugar;
    private String oficina;
    
    private Long predioId;
    private Integer noFolio;
    private Date fechaRegistro;
    
    private Date fechaGenerado;
    
    private Integer recibo;
    private BigDecimal monto;
    private String concepto;
    private String referencia;
    
    private List<PropietarioVO> propietarios;
    
    private String noLote;
    private String manzana;
    private String ubicacion;
    private String superficie;
    private String codigoPostal;
    private String predioEstado;
    private String predioMunicipio;
    private List<ColindanciaVO> colindancias;    
    
    private String gravamenes;
    private String limitaciones;
    private String avisos;
    
    private List<PasesVO> pases;
    private Float totalSegregado;
    private Float superficieRestante;
    
    private Long usuarioSolicitanteId;
    private String solicitante;
    
    private Long usuarioRegistradorId;
    private String usuarioRegistrador;
    private Long usuarioCoordinadorId;
    private String usuarioCoordinador;
    private Long usuarioAutorizoId;
    private String usuarioAutorizo;  
    private String firma;
    private String pkcs7;
    private Integer secuencia;
    private String politica;
    private String digestion;
    private Integer secuenciaTS;
	private Date estampilla;
	private String firmaCoordinador;
	private String firmaRegistrador;	
	private String historial;
	private String notario;
	private String observacion;
	private String actosVigentes;
	private String actosNoVigentes;
	
	private List<GravamenCountVO> gravamenCount;
	private List<LimitacionCountVO> limitacionCount;
	private List<LimitacionCountVO> observacionCount;
	private List<LimitacionCountVO> pasesCount;
	private String hayAviso;
	private String direccion;
	private String codVerficacion;
	
	private String noFolioFutuReg;
	private String noFolioAux;
	private String leyendaFolio;
	private String certifica;

	private List<ActoVO> sumatoriaActosPorClasifActo;

	public List<ActoVO> getSumatoriaActosPorClasifActo() {
		return sumatoriaActosPorClasifActo;
	}

	public void setSumatoriaActosPorClasifActo(List<ActoVO> sumatoriaActosPorClasifActo) {
		this.sumatoriaActosPorClasifActo = sumatoriaActosPorClasifActo;
	}
	
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getHayAviso() {
		return hayAviso;
	}
	public void setHayAviso(String hayAviso) {
		this.hayAviso = hayAviso;
	}


	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getPredioEstado() {
		return predioEstado;
	}
	public void setPredioEstado(String predioEstado) {
		this.predioEstado = predioEstado;
	}
	public String getPredioMunicipio() {
		return predioMunicipio;
	}
	public void setPredioMunicipio(String predioMunicipio) {
		this.predioMunicipio = predioMunicipio;
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
	public String getFirmaCoordinador() {
		return firmaCoordinador;
	}
	public void setFirmaCoordinador(String firmaCoordinador) {
		this.firmaCoordinador = firmaCoordinador;
	}
	public String getFirmaRegistrador() {
		return firmaRegistrador;
	}
	public void setFirmaRegistrador(String firmaRegistrador) {
		this.firmaRegistrador = firmaRegistrador;
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
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public Long getPredioId() {
		return predioId;
	}
	public void setPredioId(Long predioId) {
		this.predioId = predioId;
	}
	
	public Integer getRecibo() {
		return recibo;
	}
	public void setRecibo(Integer recibo) {
		this.recibo = recibo;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public Integer getNoFolio() {
		return noFolio;
	}
	public void setNoFolio(Integer noFolio) {
		this.noFolio = noFolio;
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
	
	public List<PropietarioVO> getPropietarios() {
		return propietarios;
	}
	public void setPropietarios(List<PropietarioVO> propietarios) {
		this.propietarios = propietarios;
	}
	public String getNoLote() {
		return noLote;
	}
	public void setNoLote(String noLote) {
		this.noLote = noLote;
	}
	public String getManzana() {
		return manzana;
	}
	public void setManzana(String manzana) {
		this.manzana = manzana;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public String getSuperficie() {
		return superficie;
	}
	public void setSuperficie(String superficie) {
		this.superficie = superficie;
	}
	
	public List<ColindanciaVO> getColindancias() {
		return colindancias;
	}
	public void setColindancias(List<ColindanciaVO> colindancias) {
		this.colindancias = colindancias;
	}
	public String getGravamenes() {
		return gravamenes;
	}
	public void setGravamenes(String gravamenes) {
		this.gravamenes = gravamenes;
	}
	public String getLimitaciones() {
		return limitaciones;
	}
	public void setLimitaciones(String limitaciones) {
		this.limitaciones = limitaciones;
	}
	public String getAvisos() {
		return avisos;
	}
	public void setAvisos(String avisos) {
		this.avisos = avisos;
	}
	public List<PasesVO> getPases() {
		return pases;
	}
	public void setPases(List<PasesVO> pases) {
		this.pases = pases;
	}
	public Float getTotalSegregado() {
		return totalSegregado;
	}
	public void setTotalSegregado(Float totalSegregado) {
		this.totalSegregado = totalSegregado;
	}
	public Float getSuperficieRestante() {
		return superficieRestante;
	}
	public void setSuperficieRestante(Float superficieRestante) {
		this.superficieRestante = superficieRestante;
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
	public String getHistorial() {
		return historial;
	}
	public void setHistorial(String historial) {
		this.historial = historial;
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
	public List<GravamenCountVO> getGravamenCount() {
		return gravamenCount;
	}
	public void setGravamenCount(List<GravamenCountVO> gravamenCount) {
		this.gravamenCount = gravamenCount;
	}
	public List<LimitacionCountVO> getLimitacionCount() {
		return limitacionCount;
	}
	public void setLimitacionCount(List<LimitacionCountVO> limitacionCount) {
		this.limitacionCount = limitacionCount;
	}
	public Long getUsuarioRegistradorId() {
		return usuarioRegistradorId;
	}
	public void setUsuarioRegistradorId(Long usuarioRegistradorId) {
		this.usuarioRegistradorId = usuarioRegistradorId;
	}
	public String getUsuarioRegistrador() {
		return usuarioRegistrador;
	}
	public void setUsuarioRegistrador(String usuarioRegistrador) {
		this.usuarioRegistrador = usuarioRegistrador;
	}
	public Long getUsuarioCoordinadorId() {
		return usuarioCoordinadorId;
	}
	public void setUsuarioCoordinadorId(Long usuarioCoordinadorId) {
		this.usuarioCoordinadorId = usuarioCoordinadorId;
	}
	public String getUsuarioCoordinador() {
		return usuarioCoordinador;
	}
	public void setUsuarioCoordinador(String usuarioCoordinador) {
		this.usuarioCoordinador = usuarioCoordinador;
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
	public String getNotario() {
		return notario;
	}
	public void setNotario(String notario) {
		this.notario = notario;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public String getOficina() {
		return oficina;
	}
	public void setOficina(String oficina) {
		this.oficina = oficina;
	}
	public String getActosVigentes() {
		return actosVigentes;
	}
	public void setActosVigentes(String actosVigentes) {
		this.actosVigentes = actosVigentes;
	}
	public String getActosNoVigentes() {
		return actosNoVigentes;
	}
	public void setActosNoVigentes(String actosNoVigentes) {
		this.actosNoVigentes = actosNoVigentes;
	}
	public String getCodVerficacion() {
		return codVerficacion;
	}
	public void setCodVerficacion(String codVerficacion) {
		this.codVerficacion = codVerficacion;
	}
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getNoFolioFutuReg() {
		return noFolioFutuReg;
	}
	public void setNoFolioFutuReg(String noFolioFutuReg) {
		this.noFolioFutuReg = noFolioFutuReg;
	}
	public String getNoFolioAux() {
		return noFolioAux;
	}
	public void setNoFolioAux(String noFolioAux) {
		this.noFolioAux = noFolioAux;
	}
	public String getLeyendaFolio() {
		return leyendaFolio;
	}
	public void setLeyendaFolio(String leyendaFolio) {
		this.leyendaFolio = leyendaFolio;
	}
	public List<LimitacionCountVO> getObservacionCount() {
		return observacionCount;
	}
	public void setObservacionCount(List<LimitacionCountVO> observacionCount) {
		this.observacionCount = observacionCount;
	}
	public List<LimitacionCountVO> getPasesCount() {
		return pasesCount;
	}
	public void setPasesCount(List<LimitacionCountVO> pasesCount) {
		this.pasesCount = pasesCount;
	}
	public String getCertifica() {
		return certifica;
	}
	public void setCertifica(String certifica) {
		this.certifica = certifica;
	}

	
}
