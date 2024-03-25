package com.gisnet.erpp.web.api.imprimeprelacion.caratula;

import com.gisnet.erpp.vo.caratula.PredioVO;

public class PasesSuperficieVO {
	private Double totalSuperficieSegregada = 0d;
	private Double superficieRestante = 0d;
	private String unidadMedida;

	public PasesSuperficieVO(PredioVO predioVO) {
		this.totalSuperficieSegregada = predioVO.getTotalSuperficieSegregada();
		this.superficieRestante = predioVO.getSuperficieRestante();
		this.unidadMedida = predioVO.getUnidadMedida();
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
	public String getUnidadMedida() {
		return unidadMedida;
	}
	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

}
