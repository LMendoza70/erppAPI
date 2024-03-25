package com.gisnet.erpp.vo.juridico;

import java.util.Date;
import java.util.List;
public class RespuestaBusquedaSimpleVO {

	private Integer busqueda;
	private Integer tipoB;
	private String  autorizo;
	private String  firmaBusqueda;
	private	String  firmaBoleta;
	private String  historial;
	private String 	reviso;

	private String  solicitud;
	private Integer numPrelacion;
	private String fecha;
	private String hora;
	private String tipoBusqueda;
	private String leyendaLocalizado;
	private String referencia;
	private String datosPFM;
	private String datosPredio;
	private String oficina;
	private String colindancias;
	private String noFolio;
	
	public String getNoOficio() {
		return noOficio;
	}

	public void setNoOficio(String noOficio) {
		this.noOficio = noOficio;
	}

	public String getFechaOficio() {
		return fechaOficio;
	}

	public void setFechaOficio(String fechaOficio) {
		this.fechaOficio = fechaOficio;
	}

	public String getAnioInicial() {
		return anioInicial;
	}

	public void setAnioInicial(String anioInicial) {
		this.anioInicial = anioInicial;
	}

	public String getAnioFinal() {
		return anioFinal;
	}

	public void setAnioFinal(String anioFinal) {
		this.anioFinal = anioFinal;
	}

	private Integer resultadoCount;
	
	private String ubicacion;
	private String noOficio;
	private String fechaOficio;
	private String anioInicial;
	private String anioFinal;


	private List<FoliosRegistralesVO> foliosRegistrales;
	private List<DatosLibroVO> datosLibros;

	 public String getSolicitud(){
	  return solicitud;
	 }
	 
	 public void setSolicitud(String solicitud){
	  this.solicitud = solicitud;
	 }

	 public Integer getNumPrelacion(){
	  return numPrelacion;
	 }
	 
	 public void setNumPrelacion(Integer numPrelacion){
	  this.numPrelacion = numPrelacion;
	 }

	 public String getFecha(){
	  return fecha;
	 }
	 
	 public void setFecha(String fecha){
	  this.fecha = fecha;
	 }

	 public String getHora(){
	  return hora;
	 }
	 
	 public void setHora(String hora){
	  this.hora = hora;
	 }

	 public String getTipoBusqueda(){
	  return tipoBusqueda;
	 }
	 
	 public void setTipoBusqueda(String tipoBusqueda){
	  this.tipoBusqueda = tipoBusqueda;
	 }

	 public String getLeyendaLocalizado(){
	  return leyendaLocalizado;
	 }
	 
	 public void setLeyendaLocalizado(String leyendaLocalizado){
	  this.leyendaLocalizado = leyendaLocalizado;
	 }

	 public String getReferencia(){
	  return referencia;
	 }
	 
	 public void setReferencia(String referencia){
	  this.referencia = referencia;
	 }



	public String getHistorial(){
		return historial;
	}
	   
	   public void setHistorial(String historial){
		this.historial = historial;
	
	}

	public String getAutorizo(){
		return autorizo;
	}
	   
	public void setAutorizo(String autorizo){
		this.autorizo = autorizo;
	}

	public String getReviso(){
		return reviso;
	}
	   
	public void setReviso(String reviso){
		this.reviso = reviso;
	}

	public String getFirmaBusqueda(){
		return firmaBusqueda;
	}
	   
	public void setFirmaBusqueda(String firmaBusqueda){
		this.firmaBusqueda = firmaBusqueda;
	}

	public String getFirmaBoleta(){
		return firmaBoleta;
	}
	   
	public void setFirmaBoleta(String firmaBoleta){
		this.firmaBoleta = firmaBoleta;
	}	



	public Integer getBusqueda(){
		return busqueda;
	}
	   
	   public void setBusqueda(Integer busqueda){
		this.busqueda = busqueda;
	
	}



	public List<FoliosRegistralesVO> getFoliosRegistrales(){
	  return foliosRegistrales;
	}
	 
	public void setFoliosRegistrales(List<FoliosRegistralesVO> foliosRegistrales){
	  this.foliosRegistrales = foliosRegistrales;
	}

	public List<DatosLibroVO> getDatosLibros(){
	  return datosLibros;
	}
	 
	 public void setDatosLibros(List<DatosLibroVO> datosLibros){
	  this.datosLibros = datosLibros;
	 }

	public String getDatosPFM() {
		return datosPFM;
	}

	public void setDatosPFM(String datosPFM) {
		this.datosPFM = datosPFM;
	}

	public String getDatosPredio() {
		return datosPredio;
	}

	public void setDatosPredio(String datosPredio) {
		this.datosPredio = datosPredio;
	}

	public Integer getResultadoCount() {
		return resultadoCount;
	}

	public void setResultadoCount(Integer resultadoCount) {
		this.resultadoCount = resultadoCount;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getOficina() {
		return oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	public String getColindancias() {
		return colindancias;
	}

	public void setColindancias(String colindancias) {
		this.colindancias = colindancias;
	}

	public String getNoFolio() {
		return noFolio;
	}

	public void setNoFolio(String noFolio) {
		this.noFolio = noFolio;
	}

	public Integer getTipoB() {
		return tipoB;
	}

	public void setTipoB(Integer tipoB) {
		this.tipoB = tipoB;
	}
	
	
}