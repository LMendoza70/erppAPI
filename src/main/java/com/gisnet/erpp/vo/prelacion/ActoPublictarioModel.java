package com.gisnet.erpp.vo.prelacion;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;

public class ActoPublictarioModel {

	private Long actoId;
	private Long actoPredioId;
	private Long actoPublicitarioId;
	private String nombre;
	private String paterno;
	private String materno;


	public Long getActoId() {
		return actoId;
	}

	public void setActoId(Long actoId) {
		this.actoId = actoId;
	}

	public Long getActoPredioId() {
		return actoPredioId;
	}

	public void setActoPredioId(Long actoPredioId) {
		this.actoPredioId = actoPredioId;
	}

	public Long getActoPublicitarioId() {
		return actoPublicitarioId;
	}

	public void setActoPublicitarioId(Long actoPublicitarioId) {
		this.actoPublicitarioId = actoPublicitarioId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPaterno() {
		return paterno;
	}

	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}

	public String getMaterno() {
		return materno;
	}

	public void setMaterno(String materno) {
		this.materno = materno;
	}
	
	
}
