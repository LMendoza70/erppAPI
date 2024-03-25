package com.gisnet.erpp.vo.caratula;

import java.io.Serializable;

import com.gisnet.erpp.domain.Acto;

public class PaseVO implements Serializable {
	private String superfice;
	private Integer noFolio;
    private Long unidadMedidaId;
    private String unidadMedidaNombre;
	private Acto acto;
	
	public PaseVO(){}
	
	public PaseVO(String superfice, Integer noFolio) {
		super();
		this.superfice = superfice;
		this.noFolio = noFolio;
	}
	public String getSuperfice() {
		return superfice;
	}
	public void setSuperfice(String superfice) {
		this.superfice = superfice;
	}
	public Integer getNoFolio() {
		return noFolio;
	}
	public void setNoFolio(Integer noFolio) {
		this.noFolio = noFolio;
	}
	
	public Long getUnidadMedidaId() {
		return unidadMedidaId;
	}

	public void setUnidadMedidaId(Long unidadMedidaId) {
		this.unidadMedidaId = unidadMedidaId;
	}

	public String getUnidadMedidaNombre() {
		return unidadMedidaNombre;
	}

	public void setUnidadMedidaNombre(String unidadMedidaNombre) {
		this.unidadMedidaNombre = unidadMedidaNombre;
	}

	public String toString() {
		return "==> PaseVO ["+
				noFolio+","+superfice+
				"]";
	}

	public Acto getActo() {
		return acto;
	}

	public void setActo(Acto acto) {
		this.acto = acto;
	}
	
	
}
