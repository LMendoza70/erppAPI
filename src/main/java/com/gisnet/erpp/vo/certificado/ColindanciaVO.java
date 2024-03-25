package com.gisnet.erpp.vo.certificado;

import java.io.Serializable;

public class ColindanciaVO implements Serializable,Comparable<ColindanciaVO> {
	private Long idOrientacion;
	private String orientacion;
	private String nombre;

	public ColindanciaVO(){}

	public ColindanciaVO(Long idOrientacion,String orientacion, String nombre) {
		super();
		this.idOrientacion = idOrientacion;
		this.orientacion = orientacion;
		this.nombre = nombre;
	}

	public Long getIdOrientacion() {
		return idOrientacion;
	}

	public void setIdOrientacion(Long idOrientacion) {
		this.idOrientacion = idOrientacion;
	}

	public String getOrientacion() {
		return orientacion;
	}

	public void setOrientacion(String orientacion) {
		this.orientacion = orientacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public int compareTo(ColindanciaVO o) {
		
		return (int) (this.idOrientacion-o.idOrientacion);
	}
	
	

}
