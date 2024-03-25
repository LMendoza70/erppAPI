package com.gisnet.erpp.web.api.aviso;

public class ParteVO {
	private String enCalidadDe;
	private String nombre;
	private String primerApellido;
	private String segundoApellido;
	public String getEnCalidadDe() {
		return enCalidadDe;
	}
	public void setEnCalidadDe(String enCalidadDe) {
		this.enCalidadDe = enCalidadDe == null ? null : enCalidadDe.trim().toUpperCase();
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre == null ? null : nombre.trim();
	}
	public String getPrimerApellido() {
		return primerApellido;
	}
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido == null ? null : primerApellido.trim().toUpperCase();
	}
	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido == null ? null : segundoApellido.trim().toUpperCase();
	}

	public String getNombreCompleto() {
		String nombreCompleto = nombre == null ? "" : nombre;
		nombreCompleto += primerApellido == null ? "" : primerApellido;
		nombreCompleto += segundoApellido == null ? "" : segundoApellido;

		return nombreCompleto;
	}

}
