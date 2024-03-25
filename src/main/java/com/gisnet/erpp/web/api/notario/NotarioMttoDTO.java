package com.gisnet.erpp.web.api.notario;

import com.gisnet.erpp.domain.Notario;

public class NotarioMttoDTO {

	private Notario notario;

	private String motivo;

	public Notario getNotario() {
		return notario;
	}

	public void setNotario(Notario notario) {
		this.notario = notario;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}
