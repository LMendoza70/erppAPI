package com.gisnet.erpp.vo.caratula;

import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.Orientacion;

import java.io.Serializable;
import java.util.Optional;

public class ColindanciaVO implements Serializable,Comparable<ColindanciaVO> {
	private Long idOrientacion;
	private String orientacion;
	private String nombre;

	public ColindanciaVO(){}

	public ColindanciaVO(Long idOrientacion,String orientacion, String nombre) {
		super();
		this.orientacion = orientacion;
		this.nombre = nombre;
		this.idOrientacion=idOrientacion;
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

	public static ColindanciaVO converToColindanciaVO(Colindancia lindero) {

		ColindanciaVO colin = new ColindanciaVO();
		colin.nombre = lindero.getNombre();
		colin.orientacion = Optional.ofNullable(lindero.getOrientacion()).map(Orientacion::getNombre).orElse("");

		return colin;
	}

	@Override
	public int compareTo(ColindanciaVO o) {
		// TODO Auto-generated method stub
		
		return (int) (this.idOrientacion-o.idOrientacion);

	}

}
