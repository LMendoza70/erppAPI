/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gisnet.erpp.vo;

/**
 *
 * @author jpena
 */
public class LibroVO {

	private Long oficina;
	private Integer anio;
	private Long seccion;
	private String tomo;//(numLibro)
	private String librobis;//(libro en front)
	private String volumen;
    private String numeroInscripcion;

    
	public Long getOficina() {
		return oficina;
	}
	public void setOficina(Long oficina) {
		this.oficina = oficina;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Long getSeccion() {
		return seccion;
	}
	public void setSeccion(Long seccion) {
		this.seccion = seccion;
	}
	public String getTomo() {
		return tomo;
	}
	public void setTomo(String tomo) {
		this.tomo = tomo;
	}
	public String getLibrobis() {
		return librobis;
	}
	public void setLibrobis(String librobis) {
		this.librobis = librobis;
	}
	public String getVolumen() {
		return volumen;
	}
	public void setVolumen(String volumen) {
		this.volumen = volumen;
	}
	public String getNumeroInscripcion() {
		return numeroInscripcion;
	}
	public void setNumeroInscripcion(String numeroInscripcion) {
		this.numeroInscripcion = numeroInscripcion;
	}

}
