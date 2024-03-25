package com.gisnet.erpp.vo.juridico;

import java.util.List;

import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.vo.PredioVO;
import com.gisnet.erpp.vo.caratula.TramiteVO;
import com.gisnet.erpp.vo.prelacion.ReciboModel;

public class CertificadoDeNoInscripcionVO{

	private String direccion;
	private String superficie;
	private String alNorte;
	private String alSur;
	private String alOriente;
	private String alPoniente;
	private String favorDe;
	private String propietarios;
	private String numSolicitud;
	private String numLibro;
	private String ciudad;
	private String horasNumero;
	private String horaLetra;
	private String dia;
	private String mesLetra;
	private String anio;
	private String numRecibo;
	private String montoNumero;
	private String montoLetra;
	private String quienBusco;
	private String abogadoAsignado;
	private String coordinadorAcargo;
	private String solicitante;
	private String fecha;
	private String numPrelacion;
	private String fechaRecepcion;
	private String fechaCertificado; 
	private String fechaPlanoCartografico;
	private String nombreDirector;
	private String municipio;
	private String cuentaCatastral;
	private String expedicion;
	private String datosPredio;
	private String numeroInscripcion;
	private String folios;
	private String directorCatastro;
	private String volumen;
	private String libroLibro;
	private String seccion;
	private String ampara;
	private String expide;
	private String derechos;
	private String recibo;
	private String elaboro;
	private String oficialRegistrador;
	private List<AntecedenteVO> antecedente;
	private List<PredioVO> predio; 
	private List<AnotacionesVO> anotaciones;
	private String anotacion;
	private List<TramiteVO> tramites;
	private List<ReciboModel> recibos;
	private String registrador;
	private String coordinador;
	private String firmaRegistrador;
	private String firmaCoordinador;
	private Integer consecutivo;
	private String oficina;
	private String procedeDe;
	
	public String getDireccion(){
		return direccion;
	}
	
	public void setDireccion(String direccion){
		this.direccion = direccion;
	}

	public String getSuperficie(){
		return superficie;
	}
	
	public void setSuperficie(String superficie){
		this.superficie = superficie;
	}

	public String getAlNorte(){
		return alNorte;
	}
	
	public void setAlNorte(String alNorte){
		this.alNorte = alNorte;
	}

	public String getAlSur(){
		return alSur;
	}
	
	public void setAlSur(String alSur){
		this.alSur = alSur;
	}

	public String getAlOriente(){
		return alOriente;
	}
	
	public void setAlOriente(String alOriente){
		this.alOriente = alOriente;
	}
	public String getAlPoniente(){
		return alPoniente;
	}
	
	public void setAlPoniente(String alPoniente){
		this.alPoniente = alPoniente;
	}

	public String getFavorDe(){
		return favorDe;
	}
	
	public void setFavorDe(String favorDe){
		this.favorDe = favorDe;
	}

	public String getPropietarios(){
		return propietarios;
	}
	
	public void setPropietarios(String propietarios){
		this.propietarios = propietarios;
	}

	public String getNumSolicitud(){
		return numSolicitud;
	}
	
	public void setNumSolicitud(String numSolicitud){
		this.numSolicitud = numSolicitud;
	}

	public String getNumLibro(){
		return numLibro;
	}
	
	public void setNumLibro(String numLibro){
		this.numLibro = numLibro;
	}

	public String getCiudad(){
		return ciudad;
	}
	
	public void setCiudad(String ciudad){
		this.ciudad = ciudad;
	}

	public String getHorasNumero(){
		return horasNumero;
	}
	
	public void setHorasNumero(String horasNumero){
		this.horasNumero = horasNumero;
	}

	public String getHoraLetra(){
		return horaLetra;
	}
	
	public void setHoraLetra(String horaLetra){
		this.horaLetra = horaLetra;
	}

	public String getDia(){
		return dia;
	}
	
	public void setDia(String dia){
		this.dia = dia;
	}

	public String getMesLetra(){
		return mesLetra;
	}
	
	public void setMesLetra(String mesLetra){
		this.mesLetra = mesLetra;
	}

	public String getAnio(){
		return anio;
	}
	
	public void setAnio(String anio){
		this.anio = anio;
	}

	public String getNumRecibo(){
		return numRecibo;
	}
	
	public void setNumRecibo(String numRecibo){
		this.numRecibo = numRecibo;
	}

	public String getMontoNumero(){
		return montoNumero;
	}
	
	public void setMontoNumero(String montoNumero){
		this.montoNumero = montoNumero;
	}

	public String getMontoLetra(){
		return montoLetra;
	}
	
	public void setMontoLetra(String montoLetra){
		this.montoLetra = montoLetra;
	}
	public String getQuienBusco(){
		return quienBusco;
	}
	
	public void setQuienBusco(String quienBusco){
		this.quienBusco = quienBusco;
	}

	public String getAbogadoAsignado(){
		return abogadoAsignado;
	}
	
	public void setAbogadoAsignado(String abogadoAsignado){
		this.abogadoAsignado = abogadoAsignado;
	}

	public String getCoordinadorAcargo(){
		return coordinadorAcargo;
	}
	
	public void setCoordinadorAcargo(String coordinadorAcargo){
		this.coordinadorAcargo = coordinadorAcargo;
	}

	/**
	 * @return the solicitante
	 */
	public String getSolicitante() {
		return solicitante;
	}

	/**
	 * @param solicitante the solicitante to set
	 */
	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the fechaRecepcion
	 */
	public String getFechaRecepcion() {
		return fechaRecepcion;
	}

	/**
	 * @param fechaRecepcion the fechaRecepcion to set
	 */
	public void setFechaRecepcion(String fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}

	/**
	 * @return the fechaPlanoCartografo
	 */
	public String getFechaPlanoCartografico() {
		return fechaPlanoCartografico;
	}

	/**
	 * @param fechaPlanoCartografo the fechaPlanoCartografo to set
	 */
	public void setFechaPlanoCartografico(String fechaPlanoCartografico) {
		this.fechaPlanoCartografico = fechaPlanoCartografico;
	}

	/**
	 * @return the fechaCertificado
	 */
	public String getFechaCertificado() {
		return fechaCertificado;
	}

	/**
	 * @param fechaCertificado the fechaCertificado to set
	 */
	public void setFechaCertificado(String fechaCertificado) {
		this.fechaCertificado = fechaCertificado;
	}

	/**
	 * @return the numPrelacion
	 */
	public String getNumPrelacion() {
		return numPrelacion;
	}

	/**
	 * @param numPrelacion the numPrelacion to set
	 */
	public void setNumPrelacion(String numPrelacion) {
		this.numPrelacion = numPrelacion;
	}

	/**
	 * @return the municipio
	 */
	public String getMunicipio() {
		return municipio;
	}

	/**
	 * @param municipio the municipio to set
	 */
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	/**
	 * @return the nombreDirector
	 */
	public String getNombreDirector() {
		return nombreDirector;
	}

	/**
	 * @param nombreDirector the nombreDirector to set
	 */
	public void setNombreDirector(String nombreDirector) {
		this.nombreDirector = nombreDirector;
	}

	/**
	 * @return the cuentaCatastral
	 */
	public String getCuentaCatastral() {
		return cuentaCatastral;
	}

	/**
	 * @param cuentaCatastral the cuentaCatastral to set
	 */
	public void setCuentaCatastral(String cuentaCatastral) {
		this.cuentaCatastral = cuentaCatastral;
	}

	public String getExpedicion() {
		return expedicion;
	}

	public void setExpedicion(String expedicion) {
		this.expedicion = expedicion;
	}

	public String getDatosPredio() {
		return datosPredio;
	}

	public void setDatosPredio(String datosPredio) {
		this.datosPredio = datosPredio;
	}

	public String getNumeroInscripcion() {
		return numeroInscripcion;
	}

	public void setNumeroInscripcion(String numeroInscripcion) {
		this.numeroInscripcion = numeroInscripcion;
	}

	public String getFolios() {
		return folios;
	}

	public void setFolios(String folios) {
		this.folios = folios;
	}

	public String getLibroLibro() {
		return libroLibro;
	}

	public void setLibroLibro(String libroLibro) {
		this.libroLibro = libroLibro;
	}

	public String getSeccion() {
		return seccion;
	}

	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	public String getAmpara() {
		return ampara;
	}

	public void setAmpara(String ampara) {
		this.ampara = ampara;
	}

	public String getExpide() {
		return expide;
	}

	public void setExpide(String expide) {
		this.expide = expide;
	}

	public String getDerechos() {
		return derechos;
	}

	public void setDerechos(String derechos) {
		this.derechos = derechos;
	}

	public String getRecibo() {
		return recibo;
	}

	public void setRecibo(String recibo) {
		this.recibo = recibo;
	}

	public String getElaboro() {
		return elaboro;
	}

	public void setElaboro(String elaboro) {
		this.elaboro = elaboro;
	}

	public String getOficialRegistrador() {
		return oficialRegistrador;
	}

	public void setOficialRegistrador(String oficialRegistrador) {
		this.oficialRegistrador = oficialRegistrador;
	}

	public String getVolumen() {
		return volumen;
	}

	public void setVolumen(String volumen) {
		this.volumen = volumen;
	}

	public List<AntecedenteVO> getAntecedente() {
		return antecedente;
	}

	public void setAntecedente(List<AntecedenteVO> antecedente) {
		this.antecedente = antecedente;
	}

	public List<PredioVO> getPredio() {
		return predio;
	}

	public void setPredio(List<PredioVO> predio) {
		this.predio = predio;
	}

	public String getDirectorCatastro() {
		return directorCatastro;
	}

	public void setDirectorCatastro(String directorCatastro) {
		this.directorCatastro = directorCatastro;
	}

	public List<AnotacionesVO> getAnotaciones() {
		return anotaciones;
	}

	public void setAnotaciones(List<AnotacionesVO> anotaciones) {
		this.anotaciones = anotaciones;
	}

	public List<TramiteVO> getTramites() {
		return tramites;
	}

	public void setTramites(List<TramiteVO> tramites) {
		this.tramites = tramites;
	}

	public String getAnotacion() {
		return anotacion;
	}

	public void setAnotacion(String anotacion) {
		this.anotacion = anotacion;
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

	public List<ReciboModel> getRecibos() {
		return recibos;
	}

	public void setRecibos(List<ReciboModel> recibos) {
		this.recibos = recibos;
	}

	public Integer getConsecutivo(){
		return consecutivo;
	}
	
	public void setDireccion(Integer consecutivo){
		this.consecutivo = consecutivo;
	}

	public String getOficina() {
		return oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	public String getProcedeDe() {
		return procedeDe;
	}

	public void setProcedeDe(String procedeDe) {
		this.procedeDe = procedeDe;
	}
	
	
}
