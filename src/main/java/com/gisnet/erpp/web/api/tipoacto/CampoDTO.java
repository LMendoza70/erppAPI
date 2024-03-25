package com.gisnet.erpp.web.api.tipoacto;

import com.gisnet.erpp.domain.ModuloCampo;


public class CampoDTO {
    private Long id;
    public String nombre;
    public String label;
    public Long tipoCampoId;
    public Long campoId;
    public Boolean requerido;
    public Boolean presencial;
    public Boolean indHist;
    public Integer minima;
    public Integer maxima;
    public Integer ancho;
    public String tabla;
	public String tablaCampo;
	public String condicionCampo;
    public String condicionTipoComparacion;
	public String condicionExprecion;
	public Integer ind_impresion;

    public CampoDTO() {
        
    }

    public CampoDTO(ModuloCampo moduloCampo) {
    	this.id = moduloCampo.getId();
    	this.nombre = moduloCampo.getCampo().getNombre();
    	this.label = moduloCampo.getEtiqueta();
    	this.presencial = moduloCampo.getPresencial();
    	this.indHist = moduloCampo.getIndHist();
    	this.tipoCampoId = moduloCampo.getCampo().getTipoCampo().getId();
    	this.campoId = moduloCampo.getCampo().getId();
    	this.requerido = moduloCampo.isRequerido();
    	this.minima = moduloCampo.getCampo().getMinima();
    	this.maxima = moduloCampo.getCampo().getMaxima();
    	this.ancho = moduloCampo.getLongitud();
    	this.tabla = moduloCampo.getCampo().getTabla();
		this.tablaCampo = moduloCampo.getCampo().getTablaCampo();
		this.condicionCampo = moduloCampo.getCondicionCampo();
    	this.condicionTipoComparacion = moduloCampo.getCondicionTipoComparacion();
		this.condicionExprecion = moduloCampo.getCondicionExprecion();
		this.ind_impresion = moduloCampo.getInd_impresion();
    	
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

	public Long getTipoCampoId() {
		return tipoCampoId;
	}

	public void setTipoCampoId(Long tipoCampoId) {
		this.tipoCampoId = tipoCampoId;
	}
	
	public Long getCampoId() {
		return campoId;
	}

	public void setCampoId(Long campoId) {
		this.campoId = campoId;
	}

	public boolean isRequerido() {
		return requerido;
	}

	public void setRequerido(boolean requerido) {
		this.requerido = requerido;
	}

	public Integer getInd_impresion() {
		return ind_impresion;
	}

	public void setInd_impresion(Integer ind_impresion) {
		this.ind_impresion = ind_impresion;
	}

}
