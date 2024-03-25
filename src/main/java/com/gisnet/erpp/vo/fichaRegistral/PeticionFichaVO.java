package com.gisnet.erpp.vo.fichaRegistral;

public class PeticionFichaVO {
	
	private Integer noFolio;
	private String 	claveCatastral;
	private String 	cuentaCatastral;
	private String 	municipio;
	
	public Integer getNoFolio() {
		return noFolio;
	}
	public void setNoFolio(Integer noFolio) {
		this.noFolio = noFolio;
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
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

}
