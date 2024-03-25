package com.gisnet.erpp.vo.fichaRegistral;

import java.util.List;

import com.gisnet.erpp.vo.caratula.ColindanciaVO;

public class FichaRegistralVO {
	
	private String 	oficina;
	private Integer noFolio;
	private String 	estatus;
	private Integer folioAnterior;
	private Integer auxiliar;
	
	private String 	tipoInmueble;
	private String 	numeroInterior;
	private String 	nivel;
	private String 	edificio;
	private String 	localidadSector;
	private String 	noLote;
	private String 	fraccion;
	private String 	manzana;
	
	private String 	tipoVialidad;
	private String 	vialidad;
	private String 	numExt;
	private String 	enlaceVialidad;
	private String 	tipoVialidad2;
	private String 	vialidad2;
	private String 	numExt2;
	
	private String 	tipoAsentamiento;
	private String 	asentamiento;	
	//private String 	fracOCondo;
	private String 	nombreFracOCondo;
	private String 	etapaOSeccion;
	
	private String 	zona;
	private String 	claveCatastral;	
	private String 	cuentaCatastral;
	private String 	claveCatastralEstandard;
	
	private String 	estado;
	private String 	municipio;
	private String 	cp;
	private String 	superficie;
	private String 	unidadMedida;
	private Double 	superficieM2;
	private String 	usoSuelo;
	
	private List<MedidasVO> colindancias;
	private List<PaseFichaVO> 	pases;
	private List<TitularesVO> 	titulares;
	
	public String getOficina() {
		return oficina;
	}
	public void setOficina(String oficina) {
		this.oficina = oficina;
	}
	public Integer getNoFolio() {
		return noFolio;
	}
	public void setNoFolio(Integer noFolio) {
		this.noFolio = noFolio;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public Integer getFolioAnterior() {
		return folioAnterior;
	}
	public void setFolioAnterior(Integer folioAnterior) {
		this.folioAnterior = folioAnterior;
	}
	public Integer getAuxiliar() {
		return auxiliar;
	}
	public void setAuxiliar(Integer auxiliar) {
		this.auxiliar = auxiliar;
	}
	public String getTipoInmueble() {
		return tipoInmueble;
	}
	public void setTipoInmueble(String tipoInmueble) {
		this.tipoInmueble = tipoInmueble;
	}
	public String getNumeroInterior() {
		return numeroInterior;
	}
	public void setNumeroInterior(String numeroInterior) {
		this.numeroInterior = numeroInterior;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getEdificio() {
		return edificio;
	}
	public void setEdificio(String edificio) {
		this.edificio = edificio;
	}
	public String getLocalidadSector() {
		return localidadSector;
	}
	public void setLocalidadSector(String localidadSector) {
		this.localidadSector = localidadSector;
	}
	public String getNoLote() {
		return noLote;
	}
	public void setNoLote(String noLote) {
		this.noLote = noLote;
	}
	public String getFraccion() {
		return fraccion;
	}
	public void setFraccion(String fraccion) {
		this.fraccion = fraccion;
	}
	public String getManzana() {
		return manzana;
	}
	public void setManzana(String manzana) {
		this.manzana = manzana;
	}
	public String getTipoVialidad() {
		return tipoVialidad;
	}
	public void setTipoVialidad(String tipoVialidad) {
		this.tipoVialidad = tipoVialidad;
	}
	public String getVialidad() {
		return vialidad;
	}
	public void setVialidad(String vialidad) {
		this.vialidad = vialidad;
	}
	public String getNumExt() {
		return numExt;
	}
	public void setNumExt(String numExt) {
		this.numExt = numExt;
	}
	public String getEnlaceVialidad() {
		return enlaceVialidad;
	}
	public void setEnlaceVialidad(String enlaceVialidad) {
		this.enlaceVialidad = enlaceVialidad;
	}
	public String getTipoVialidad2() {
		return tipoVialidad2;
	}
	public void setTipoVialidad2(String tipoVialidad2) {
		this.tipoVialidad2 = tipoVialidad2;
	}
	public String getVialidad2() {
		return vialidad2;
	}
	public void setVialidad2(String vialidad2) {
		this.vialidad2 = vialidad2;
	}
	public String getNumExt2() {
		return numExt2;
	}
	public void setNumExt2(String numExt2) {
		this.numExt2 = numExt2;
	}
	public String getTipoAsentamiento() {
		return tipoAsentamiento;
	}
	public void setTipoAsentamiento(String tipoAsentamiento) {
		this.tipoAsentamiento = tipoAsentamiento;
	}
	public String getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(String asentamiento) {
		this.asentamiento = asentamiento;
	}
	public String getNombreFracOCondo() {
		return nombreFracOCondo;
	}
	public void setNombreFracOCondo(String nombreFracOCondo) {
		this.nombreFracOCondo = nombreFracOCondo;
	}
	public String getEtapaOSeccion() {
		return etapaOSeccion;
	}
	public void setEtapaOSeccion(String etapaOSeccion) {
		this.etapaOSeccion = etapaOSeccion;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	public String getClaveCatastral() {
		return claveCatastral;
	}
	public void setClaveCatastral(String claveCatastral) {
		this.claveCatastral = claveCatastral;
	}
	public String getCuentaCatastral() {
		return cuentaCatastral;
	}
	public void setCuentaCatastral(String cuentaCatastral) {
		this.cuentaCatastral = cuentaCatastral;
	}
	public String getClaveCatastralEstandard() {
		return claveCatastralEstandard;
	}
	public void setClaveCatastralEstandard(String claveCatastralEstandard) {
		this.claveCatastralEstandard = claveCatastralEstandard;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public String getSuperficie() {
		return superficie;
	}
	public void setSuperficie(String superficie) {
		this.superficie = superficie;
	}
	public String getUnidadMedida() {
		return unidadMedida;
	}
	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}
	public Double getSuperficieM2() {
		return superficieM2;
	}
	public void setSuperficieM2(Double superficieM2) {
		this.superficieM2 = superficieM2;
	}
	public String getUsoSuelo() {
		return usoSuelo;
	}
	public void setUsoSuelo(String usoSuelo) {
		this.usoSuelo = usoSuelo;
	}
	public List<MedidasVO> getColindancias() {
		return colindancias;
	}
	public void setColindancias(List<MedidasVO> colindancias) {
		this.colindancias = colindancias;
	}
	public List<PaseFichaVO> getPases() {
		return pases;
	}
	public void setPases(List<PaseFichaVO> pases) {
		this.pases = pases;
	}
	public List<TitularesVO> getTitulares() {
		return titulares;
	}
	public void setTitulares(List<TitularesVO> titulares) {
		this.titulares = titulares;
	}
	

}
