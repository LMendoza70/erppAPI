package com.gisnet.erpp.service.materializacion;

import com.gisnet.erpp.domain.Prelacion;

public class PrelacionVO {
	private Prelacion prelacion;
	private Boolean seleccionada;

	public Prelacion getPrelacion() {
		return prelacion;
	}

	public void setPrelacion(Prelacion prelacion) {
		this.prelacion = prelacion;
	}

	public Boolean getSeleccionada() {
		return seleccionada;
	}

	public void setSeleccionada(Boolean seleccionada) {
		this.seleccionada = seleccionada;
	}
}
