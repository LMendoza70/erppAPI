package com.gisnet.erpp.web.api.imprimeprelacion.caratula;

import com.gisnet.erpp.vo.caratula.PredioVO;

public class EstatusInmuebleVO {
	private String estatus;

	public EstatusInmuebleVO(PredioVO predio) {
		this.estatus = predio.isLibre() ? "Libre" : "Gravado";
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

}
