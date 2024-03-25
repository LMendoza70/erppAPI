package com.gisnet.erpp.web.api.imprimeprelacion.caratula;

import java.util.List;

public class ValoresActo {
	private List<ModuloVO> modulos;
	private String nombreActo;

	public List<ModuloVO> getModulos() {
		return modulos;
	}

	public void setModulos(List<ModuloVO> modulos) {
		this.modulos = modulos;
	}

	public String getNombreActo() {
		return nombreActo;
	}

	public void setNombreActo(String nombreActo) {
		this.nombreActo = nombreActo;
	}

}
