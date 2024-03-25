package com.gisnet.erpp.vo;

import javax.persistence.ManyToOne;

import com.gisnet.erpp.domain.*;

public class PredioVO { 
	private Long          id;
    private String        noLote;
    private String        manzana;
    private String		  macroManzana;
    private Float         superficie;
    private Double 		  superficieM2;
    private String        claveCatastral;
    private String        numExt;
    private String        numExt2;
    private String        numInt;
    private String        cuentaCatastral;
    private String        claveCatastralEstandard;
    private Nivel         nivel;
    private String        edificio;
    private String       fraccion;
	private PredioAnte    predioAntesParaPredio;
    private UsoSuelo      usoSuelo;
    private UnidadMedida  unidadMedida;
    private String        vialidad;
    private String        vialidad2;
    private String        asentamiento;
    private String        zona;
    private Colindancia[] colindancias;
    private Integer       noFolio;
    private String		  localidadSector;
    private StatusActo    statusActo;
    private Boolean       caratulaActualizada;
    private Prelacion     prelacion;
    private Boolean       bloqueado;
    private Oficina       oficina;
    private String		  procedeDeFolio;
	private Integer 	  primerRegistro;
	private TipoVialidad  tipoVialidad;
	private TipoVialidad  tipoVialidad2;
	private TipoAsent	  tipoAsent;
	private TipoInmueble  tipoInmueble;
	private EnlaceVialidad enlaceVialidad;
	private Municipio 	  municipio;
	private String		  codigoPostal;
	private String 		  nombreFracOCondo;
	
	private Integer       numeroFolioReal;
	private Integer       auxiliar;

	private Long idPrelacionPredio;
    
	public Colindancia[] getColindancias() {
		return colindancias;
	}
	public void setColindancias(Colindancia[] colindancias) {
		this.colindancias = colindancias;
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
	public Float getSuperficie() {
		return superficie;
	}
	public void setSuperficie(Float superficie) {
		this.superficie = superficie;
	}
	public String getClaveCatastral() {
		return claveCatastral;
	}
	public void setClaveCatastral(String claveCatastral) {
		this.claveCatastral = claveCatastral;
	}
	public String getNumExt() {
		return numExt;
	}
	public void setNumExt(String numExt) {
		this.numExt = numExt;
	}
	public String getNumInt() {
		return numInt;
	}
	public void setNumInt(String numInt) {
		this.numInt = numInt;
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
	public Nivel getNivel() {
		return nivel;
	}
	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	
	public String getEdificio() {
		return edificio;
	}
	public void setEdificio(String edificio) {
		this.edificio = edificio;
	}
	public String getFraccion() {
		return fraccion;
	}
	public void setFraccion(String fraccion) {
		this.fraccion = fraccion;
	}
	public PredioAnte getPredioAntesParaPredio() {
		return predioAntesParaPredio;
	}
	public void setPredioAntesParaPredio(PredioAnte predioAntesParaPredio) {
		this.predioAntesParaPredio = predioAntesParaPredio;
	}
	public UsoSuelo getUsoSuelo() {
		return usoSuelo;
	}
	public void setUsoSuelo(UsoSuelo usoSuelo) {
		this.usoSuelo = usoSuelo;
	}
	public UnidadMedida getUnidadMedida() {
		return unidadMedida;
	}
	public void setUnidadMedida(UnidadMedida unidadMedida) {
		this.unidadMedida = unidadMedida;
	}
	public String getVialidad() {
		return vialidad;
	}
	public void setVialidad(String vialidad) {
		this.vialidad = vialidad;
	}
	public String getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(String asentamiento) {
		this.asentamiento = asentamiento;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getNoFolio() {
		return noFolio;
	}
	public void setNoFolio(Integer noFolio) {
		this.noFolio = noFolio;
	}
	
	public StatusActo getStatusActo() {
		return statusActo;
	}
	public void setStatusActo(StatusActo statusActo) {
		this.statusActo = statusActo;
	}
	public Long getIdPrelacionPredio(){

		return this.idPrelacionPredio;
	}

	public void setIdPrelacionPredio(Long id){

		this.idPrelacionPredio=id;
	}
	public Boolean getCaratulaActualizada() {
		return caratulaActualizada;
	}
	public void setCaratulaActualizada(Boolean caratulaActualizada) {
		this.caratulaActualizada = caratulaActualizada;
	}
	public Prelacion getPrelacion() {
		return prelacion;
	}
	public void setPrelacion(Prelacion prelacion) {
		this.prelacion = prelacion;
	}
	
	public Boolean getBloqueado() {
		return this.bloqueado;
	}
	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}
	public String getProcedeDeFolio() {
		return procedeDeFolio;
	}
	public void setProcedeDeFolio(String procedeDeFolio) {
		this.procedeDeFolio = procedeDeFolio;
	}
	public Integer getPrimerRegistro() {
		return primerRegistro;
	}
	public void setPrimerRegistro(Integer primerRegistro) {
		this.primerRegistro = primerRegistro;
	}

	public Integer getNumeroFolioReal() {
		return numeroFolioReal;
	}
	public void setNumeroFolioReal(Integer numeroFolioReal) {
		this.numeroFolioReal = numeroFolioReal;
	}

	public Integer getAuxiliar() {
		return auxiliar;
	}
	public void setAuxiliar(Integer auxiliar) {
		this.auxiliar = auxiliar;
	}
	public String getMacroManzana() {
		return macroManzana;
	}
	public void setMacroManzana(String macroManzana) {
		this.macroManzana = macroManzana;
	}
	public Double getSuperficieM2() {
		return superficieM2;
	}
	public void setSuperficieM2(Double superficieM2) {
		this.superficieM2 = superficieM2;
	}
	public String getNumExt2() {
		return numExt2;
	}
	public void setNumExt2(String numExt2) {
		this.numExt2 = numExt2;
	}
	public String getVialidad2() {
		return vialidad2;
	}
	public void setVialidad2(String vialidad2) {
		this.vialidad2 = vialidad2;
	}
	public String getLocalidadSector() {
		return localidadSector;
	}
	public void setLocalidadSector(String localidadSector) {
		this.localidadSector = localidadSector;
	}
	public TipoVialidad getTipoVialidad() {
		return tipoVialidad;
	}
	public void setTipoVialidad(TipoVialidad tipoVialidad) {
		this.tipoVialidad = tipoVialidad;
	}
	public TipoVialidad getTipoVialidad2() {
		return tipoVialidad2;
	}
	public void setTipoVialidad2(TipoVialidad tipoVialidad2) {
		this.tipoVialidad2 = tipoVialidad2;
	}
	public TipoAsent getTipoAsent() {
		return tipoAsent;
	}
	public void setTipoAsent(TipoAsent tipoAsent) {
		this.tipoAsent = tipoAsent;
	}
	public TipoInmueble getTipoInmueble() {
		return tipoInmueble;
	}
	public void setTipoInmueble(TipoInmueble tipoInmueble) {
		this.tipoInmueble = tipoInmueble;
	}
	public EnlaceVialidad getEnlaceVialidad() {
		return enlaceVialidad;
	}
	public void setEnlaceVialidad(EnlaceVialidad enlaceVialidad) {
		this.enlaceVialidad = enlaceVialidad;
	}
	public Municipio getMunicipio() {
		return municipio;
	}
	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getNombreFracOCondo() {
		return nombreFracOCondo;
	}
	public void setNombreFracOCondo(String nombreFracOCondo) {
		this.nombreFracOCondo = nombreFracOCondo;
	}
}
