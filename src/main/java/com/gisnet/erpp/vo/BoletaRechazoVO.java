package com.gisnet.erpp.vo;

import   java.util.Date;
import   java.util.List; 
import   java.math.BigDecimal;



public  class BoletaRechazoVO {


    public Long        prelacionId;;  
    public Integer     noFolio;;
    public String      fechaRegistro;   
    public BigDecimal  montoDerechos;
    public String      areaFolio="";
    public String      noFedatario=""; 
    public String      acto="";
    public String      ubicacion="";      
    public String      rfc="" ;  
    public String      fechaRechazo ;     
    public String      razonRechazo="" ;     
    public String      fundamento="";      
    public BigDecimal  montoOperacion;
    public String      solicitante=""  ;    
    public String      usuarioAutorizo="" ;   
    public String      comentarios=""  ;    
	public String      firmaCoordinador="" ; 
	private String     firmaRegistrador = ""; 
    public Integer     dias; 
    public String      revisoElaboro="" ;  
    public String      oficina="";
    



		public Long getPrelacionId() {
			return prelacionId;
		}
		public void setPrelacionId(Long prelacionId) {
			this.prelacionId = prelacionId;
		}
		public Integer getNoFolio() {
			return noFolio;
		}
		public void setNoFolio(Integer noFolio) {
			this.noFolio = noFolio;
		}
		public String getFechaRegistro() {
			return fechaRegistro;
		}
		public void setFechaRegistro(String fechaRegistro) {
			this.fechaRegistro = fechaRegistro;
		}
		public BigDecimal getMontoDerechos() {
			return montoDerechos;
		}
		public void setMontoDerechos(BigDecimal montoDerechos) {
			this.montoDerechos = montoDerechos;
		}
		public String getAreaFolio() {
			return areaFolio;
		}
		public void setAreaFolio(String areaFolio) {
			this.areaFolio = areaFolio;
		}
		public String getNoFedatario() {
			return noFedatario;
		}
		public void setNoFedatario(String noFedatario) {
			this.noFedatario = noFedatario;
		}
		public String getActo() {
			return acto;
		}
		public void setActo(String acto) {
			this.acto = acto;
		}
		public String getUbicacion() {
			return ubicacion;
		}
		public void setUbicacion(String ubicacion) {
			this.ubicacion = ubicacion;
		}
		public String getRfc() {
			return rfc;
		}
		public void setRfc(String rfc) {
			this.rfc = rfc;
		}
		public String getFechaRechazo() {
			return fechaRechazo;
		}
		public void setFechaRechazo(String fechaRechazo) {
			this.fechaRechazo = fechaRechazo;
		}
		public String getRazonRechazo() {
			return razonRechazo;
		}
		public void setRazonRechazo(String razonRechazo) {
			this.razonRechazo = razonRechazo;
		}
		public String getFundamento() {
			return fundamento;
		}
		public void setFundamento(String fundamento) {
			this.fundamento = fundamento;
		}
		public BigDecimal getMontoOperacion() {
			return montoOperacion;
		}
		public void setMontoOperacion(BigDecimal montoOperacion) {
			this.montoOperacion = montoOperacion;
		}
		public String getSolicitante() {
			return solicitante;
		}
		public void setSolicitante(String solicitante) {
			this.solicitante = solicitante;
		}
		public String getUsuarioAutorizo() {
			return usuarioAutorizo;
		}
		public void setUsuarioAutorizo(String usuarioAutorizo) {
			this.usuarioAutorizo = usuarioAutorizo;
		}
		public String getComentarios() {
			return comentarios;
		}
		public void setComentarios(String comentarios) {
			this.comentarios = comentarios;
		}
		public String getFirmaCoordinador() {
			return firmaCoordinador;
		}
		public void setFirmaCoordinador(String firmaCoordinador) {
			this.firmaCoordinador = firmaCoordinador;
		}
		public Integer getDias() {
			return dias;
		}
		public void setDias(Integer dias) {
			this.dias = dias;
		}
		public String getRevisoElaboro() {
			return revisoElaboro;
		}
		public void setRevisoElaboro(String revisoElaboro) {
			this.revisoElaboro = revisoElaboro;
		}   
	
		public String getFirmaRegistrador() {
			return firmaRegistrador;
		}
		public void setFirmaRegistrador(String firmaRegistrador) {
			this.firmaRegistrador = firmaRegistrador;
		}
		public String getOficina() {
			return oficina;
		}
		public void setOficina(String oficina) {
			this.oficina = oficina;
		}   
		



}

