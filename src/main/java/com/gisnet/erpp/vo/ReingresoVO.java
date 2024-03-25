package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.Motivo;

public class ReingresoVO {
	
  private Motivo motivo;
  private String fundamento;
  private Long prelacionId;
  
	public Motivo getMotivo() {
		return motivo;
	}
	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}
	public String getFundamento() {
		return fundamento;
	}
	public void setFundamento(String fundamento) {
		this.fundamento = fundamento;
	}
	public Long getPrelacionId() {
		return prelacionId;
	}
	public void setPrelacionId(Long prelacionId) {
		this.prelacionId = prelacionId;
	}
  
  
}
