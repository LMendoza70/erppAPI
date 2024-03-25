package com.gisnet.erpp.vo.certificado;

import java.io.Serializable;

public class PasesVO implements Serializable {
	private String superfice;
	private Integer noFolio;
		
	public PasesVO(){}
	
	public PasesVO(String superfice, Integer noFolio) {
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
	
	
}
