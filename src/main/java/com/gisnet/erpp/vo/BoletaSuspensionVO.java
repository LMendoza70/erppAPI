package com.gisnet.erpp.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


//JADV-SUSPENCION
public class BoletaSuspensionVO {

		private Long        prelacionId;
	 	private Integer     noFolio;; //
	    private Date        fechaRegistro;  
	    private String      solicitante="";
	    private Date        fechaSuspension;  
		private Date        fechaVencimiento;  
		private Date        fechaFirma;
	    private String      razonSuspension="" ; 
	    private String      fundamento="";
	    private String      comentarios=""  ;  
	    private Integer     dias; 
	    private String revisoElaboro = "";
	    private String usuarioAutorizo = "";
	    private String firma = "";
	    private List<PredioActoVO> predioActo;
	    private String oficina ="";
	    
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
		public Date getFechaRegistro() {
			return fechaRegistro;
		}
		public void setFechaRegistro(Date fechaRegistro) {
			this.fechaRegistro = fechaRegistro;
		}
		public String getSolicitante() {
			return solicitante;
		}
		public void setSolicitante(String solicitante) {
			this.solicitante = solicitante;
		}
		public Date getFechaSuspension() {
			return fechaSuspension;
		}
		public void setFechaSuspension(Date fechaSuspension) {
			this.fechaSuspension = fechaSuspension;
		}
		public String getRazonSuspension() {
			return razonSuspension;
		}
		public void setRazonSuspension(String razonSuspension) {
			this.razonSuspension = razonSuspension;
		}
		public String getFundamento() {
			return fundamento;
		}
		public void setFundamento(String fundamento) {
			this.fundamento = fundamento;
		}
		public String getComentarios() {
			return comentarios;
		}
		public void setComentarios(String comentarios) {
			this.comentarios = comentarios;
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
		public String getFirma() {
			return firma;
		}
		public void setFirma(String firma) {
			this.firma = firma;
		}
		public String getUsuarioAutorizo() {
			return usuarioAutorizo;
		}
		public void setUsuarioAutorizo(String usuarioAutorizo) {
			this.usuarioAutorizo = usuarioAutorizo;
		}
		public Date getFechaVencimiento() {
			return fechaVencimiento;
		}
		public void setFechaVencimiento(Date fechaVencimiento) {
			this.fechaVencimiento = fechaVencimiento;
		}
		public List<PredioActoVO> getPredioActo() {
			return predioActo;
		}
		public void setPredioActo(List<PredioActoVO> predioActo) {
			this.predioActo = predioActo;
		}
		public String getOficina() {
			return oficina;
		}
		public void setOficina(String oficina) {
			this.oficina = oficina;
		}   
	    public Date getFechaFirma() {
			return fechaFirma;
		}
		public void setFechaFirma(Date fechaFirma) {
			this.fechaFirma = fechaFirma;
		}
	    

	
	
}
