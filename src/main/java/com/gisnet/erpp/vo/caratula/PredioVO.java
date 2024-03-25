package com.gisnet.erpp.vo.caratula;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.PredioBitacora;
import com.gisnet.erpp.domain.StatusActo;
public class PredioVO {
	private Long predioId;
	
	private String procedeFolio;
	private boolean primerRegistro;
	private Integer noFolio;
	private Integer noFolioAnterior;
	private Integer auxiliar;
	
	private AntecedenteVO antecedente;
	
	private List<AntecedenteVO> antecedentes;
	
	private String tipoInmueble;
	private String numeroInterior;
	private String nivel;
	private String edificio;
	private String localidadSector;
	private String noLote;
	private String fraccion;
	private String manzana;
	private String macroManzana;
	
	private String tipoVialidad;
	private String vialidad;
	private String numExt;
	private String enlaceVialidad;
	private String numInt;
	private String tipoVialidad2;
	private String vialidad2;
	private String numExt2;
	
	private String tipoAsentamiento;
	private String asentamiento;	
	private String fracOCondo;
	private String nombreFracOCondo;
	private String etapaOSeccion;
	
	private String zona;
	private String claveCatastral;	
	private String cuentaCatastral;
	private String claveCatastralEstandard;
	
	private String estado;
	private String municipio;
	private String cp;
	private String superficie;
	private String unidadMedida;
	private Double superficieM2;
	private String usoSuelo;
	
	private Long oficinaId;
	private StatusActo statusActo;
	private String nomOficina;
	private List<PredioVO> resultantes;
	
    private Boolean bloqueado = new Boolean(false);

	
	private List<ColindanciaVO> colindancias;
	private List<PaseVO> pases;
	private List<PropietarioVO> propietarios;
	
	private Double totalSuperficieSegregada = 0d;
	private Double superficieRestante = 0d;
	
	private boolean libre;
	private boolean extinto;
	
	private List<ActoVO> sumatoriaActosPorClasifActo;
	
	private List<TramiteVO> tramites;
	
	private List<DevArt71VO> devoluciones;

	private boolean enProceso;
	
	private List<PredioBitacora> predioBitacora;

	private String indMatriz;
	private String indAuxProcedeDe;

	private Boolean folioCalidad;

	private Set<ActoPredio> actoPrediosParaPredios = new HashSet<>();

	public Set<ActoPredio> getActoPrediosParaPredios() {
		return actoPrediosParaPredios;
	}

	public void setActoPrediosParaPredios(Set<ActoPredio> actoPrediosParaPredios) {
		this.actoPrediosParaPredios = actoPrediosParaPredios;
	}

	public List<ActoVO> getSumatoriaActosPorClasifActo() {
		return sumatoriaActosPorClasifActo;
	}

	public void setSumatoriaActosPorClasifActo(List<ActoVO> sumatoriaActosPorClasifActo) {
		this.sumatoriaActosPorClasifActo = sumatoriaActosPorClasifActo;
	}

	public String getProcedeFolio() {
		return procedeFolio;
	}

	public void setProcedeFolio(String procedeFolio) {
		this.procedeFolio = procedeFolio;
	}

	public AntecedenteVO getAntecedente() {
		return antecedente;
	}

	public void setAntecedente(AntecedenteVO antecedente) {
		this.antecedente = antecedente;
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
	
	public Double getSuperficieM2() {
		return superficieM2;
	}

	public void setSuperficieM2(Double superficieM2) {
		this.superficieM2 = superficieM2;
	}

	public String getFraccion() {
		return fraccion;
	}

	public void setFraccion(String fraccion) {
		this.fraccion = fraccion;
	}

	public void setMacroManzana(String macroManzana) {
		this.macroManzana = macroManzana;
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

	public String getNumInt() {
		return numInt;
	}

	public void setNumInt(String numInt) {
		this.numInt = numInt;
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

	public String getMacroManzana() {
		return macroManzana;
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

	public String getFracOCondo() {
		return fracOCondo;
	}

	public void setFracOCondo(String fracOCondo) {
		this.fracOCondo = fracOCondo;
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


	public String getUsoSuelo() {
		return usoSuelo;
	}

	public void setUsoSuelo(String usoSuelo) {
		this.usoSuelo = usoSuelo;
	}

	public List<ColindanciaVO> getColindancias() {
		return colindancias;
	}

	public void setColindancias(List<ColindanciaVO> colindancias) {
		this.colindancias = colindancias;
	}

	public List<PaseVO> getPases() {
		return pases;
	}

	public void setPases(List<PaseVO> pases) {
		this.pases = pases;
	}

	public List<PropietarioVO> getPropietarios() {
		return propietarios;
	}

	public void setPropietarios(List<PropietarioVO> propietarios) {
		this.propietarios = propietarios;
	}

	public boolean isLibre() {
		return libre;
	}

	public void setLibre(boolean libre) {
		this.libre = libre;
	}

	public List<TramiteVO> getTramites() {
		return tramites;
	}

	public void setTramites(List<TramiteVO> tramites) {
		this.tramites = tramites;
	}

	public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public Long getPredioId() {
		return predioId;
	}

	public void setPredioId(Long predioId) {
		this.predioId = predioId;
	}

	public Double getTotalSuperficieSegregada() {
		return totalSuperficieSegregada;
	}

	public void setTotalSuperficieSegregada(Double totalSuperficieSegregada) {
		this.totalSuperficieSegregada = totalSuperficieSegregada;
	}

	public Double getSuperficieRestante() {
		return superficieRestante;
	}

	public void setSuperficieRestante(Double superficieRestante) {
		this.superficieRestante = superficieRestante;
	}

	public List<DevArt71VO> getDevoluciones() {
		return devoluciones;
	}

	public void setDevoluciones(List<DevArt71VO> devoluciones) {
		this.devoluciones = devoluciones;
	}

	public boolean isPrimerRegistro() {
		return primerRegistro;
	}

	public void setPrimerRegistro(boolean primerRegistro) {
		this.primerRegistro = primerRegistro;
	}

	public boolean isEnProceso() {
		return enProceso;
	}

	public void setEnProceso(boolean enProceso) {
		this.enProceso = enProceso;
	}

	public Integer getNoFolio() {
		return noFolio;
	}

	public void setNoFolio(Integer noFolio) {
		this.noFolio = noFolio;
	}

	public Long getOficinaId() {
		return oficinaId;
	}

	public void setOficinaId(Long oficinaId) {
		this.oficinaId = oficinaId;
	}

	public StatusActo getstatusActo() {
		return statusActo;
	}

	public void setstatusActo(StatusActo statusActo) {
		this.statusActo = statusActo;
	}

	
	
	public String getNomOficina() {
		return nomOficina;
	}

	public void setNomOficina(String nomOficina) {
		this.nomOficina = nomOficina;
	}
	
	public Integer getNoFolioAnterior() {
		return noFolioAnterior;
	}

	public void setNoFolioAnterior(Integer noFolioAnterior) {
		this.noFolioAnterior = noFolioAnterior;
	}

	public Integer getAuxiliar() {
		return auxiliar;
	}

	public void setAuxiliar(Integer auxiliar) {
		this.auxiliar = auxiliar;
	}

	public List<PredioVO> getResultantes() {
		return resultantes;
	}

	public void setResultantes(List<PredioVO> resultantes) {
		this.resultantes = resultantes;
	}

	public boolean isExtinto() {
		return extinto;
	}

	public void setExtinto(boolean extinto) {
		this.extinto = extinto;
	}

	public List<PredioBitacora> getPredioBitacora() {
		return predioBitacora;
	}

	public void setPredioBitacora(List<PredioBitacora> predioBitacora) {
		this.predioBitacora = predioBitacora;
	}

	public List<AntecedenteVO> getAntecedentes() {
		return antecedentes;
	}

	public void setAntecedentes(List<AntecedenteVO> antecedentes) {
		this.antecedentes = antecedentes;
	}

	public String getIndMatriz() {
		return indMatriz;
	}

	public void setIndMatriz(String indMatriz) {
		this.indMatriz = indMatriz;
	}

	public String getIndAuxProcedeDe() {
		return indAuxProcedeDe;
	}

	public void setIndAuxProcedeDe(String indAuxProcedeDe) {
		this.indAuxProcedeDe = indAuxProcedeDe;
	}

	public Boolean getFolioCalidad() {
		return folioCalidad;
	}

	public void setFolioCalidad(Boolean folioCalidad) {
		this.folioCalidad = folioCalidad;
	}
	
	

	
}
