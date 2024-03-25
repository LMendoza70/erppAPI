package com.gisnet.erpp.web.api.tipoacto;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.gisnet.erpp.domain.ModuloCampo;
import com.gisnet.erpp.domain.ModuloTipoActo;
import com.gisnet.erpp.util.Constantes;


public class ModuloDTO {
    private Long id;
    public String nombre;
    public String label;
    public Integer orden;
    public Integer numColumnas;
    public String condicionModulo;
    public String condicionCampo;
    public String condicionTipoComparacion;
    public String condicionExprecion;
    public Boolean multiple;
    public Boolean presencial;
    public Integer cf;
    public Long tipoModuloId;
    public Integer comportamientoModulo;
    public Set<CampoDTO> campos;
    private Boolean hist;
	private Boolean ve;
	private Integer ind_impresion;

    public ModuloDTO() {
    }

    public ModuloDTO(ModuloTipoActo moduloTipoActo) {
    	this.id = moduloTipoActo.getId();
    	this.nombre = moduloTipoActo.getModulo().getNombre();
		this.label = moduloTipoActo.getEtiqueta();
		this.presencial = moduloTipoActo.getPresencial();
		this.cf = moduloTipoActo.getCf()!= null ? moduloTipoActo.getCf() : 0;
    	this.orden = moduloTipoActo.getOrden();
    	this.numColumnas = moduloTipoActo.getNumColumnas();
    	this.condicionModulo = moduloTipoActo.getCondicionModulo();
    	this.condicionCampo = moduloTipoActo.getCondicionCampo();
    	this.condicionTipoComparacion = moduloTipoActo.getCondicionTipoComparacion();
    	this.condicionExprecion = moduloTipoActo.getCondicionExprecion();
    	this.multiple = moduloTipoActo.isMultiple();
    	this.tipoModuloId = moduloTipoActo.getModulo().getId();
		this.comportamientoModulo = moduloTipoActo.getModulo().getComportamientoModulo() != null ? moduloTipoActo.getModulo().getComportamientoModulo() : 0;
		this.ind_impresion= moduloTipoActo.getInd_impresion();
    	Set<ModuloCampo> modulosCampo = moduloTipoActo.getModulo().getModuloCamposParaModulos();
    	Set<CampoDTO> newCampos = new LinkedHashSet<CampoDTO>();
    	for (Iterator<ModuloCampo> iterator = modulosCampo.iterator(); iterator.hasNext();) {
			ModuloCampo moduloCampo = (ModuloCampo) iterator.next();
			CampoDTO campoDTO = new CampoDTO(moduloCampo);			
			newCampos.add(campoDTO);			
			
			if (campoDTO.getTipoCampoId() == Constantes.TipoCampo.LISTA_ACTOS_POR_TIPO_ACTO_TIPO_ACTO.getIdTipoCampo()) {
				this.comportamientoModulo = 18;
			}		
			
		}
    	    	
    	this.campos = newCampos;
    	this.hist = moduloTipoActo.getHist();
    	this.ve = moduloTipoActo.getVe();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public Long getTipoModuloId() {
		return tipoModuloId;
	}

	public void setTipoModuloId(Long tipoModuloId) {
		this.tipoModuloId = tipoModuloId;
	}

	public Set<CampoDTO> getCampos() {
		return campos;
	}

	public void setCampos(Set<CampoDTO> campos) {
		this.campos = campos;
	}

	public Boolean getHist() {
		return hist;
	}

	public void setHist(Boolean hist) {
		this.hist = hist;
	}

	public Boolean getVe() {
		return ve;
	}

	public void setVe(Boolean ve) {
		this.ve = ve;
	}

	public Integer getInd_impresion() {
		return ind_impresion;
	}

	public void setInd_impresion(Integer ind_impresion) {
		this.ind_impresion = ind_impresion;
	}



}
