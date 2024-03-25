package com.gisnet.erpp.web.api.aviso;

public class PersonaVO {
	private String nombre;
	private String primerApellido;
	private String segundoApellido;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre == null ? null : nombre.trim().toUpperCase();
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

	public boolean cumpleParte(ParteVO parte) {
		String nombreCompletoParte = parte.getNombreCompleto();
		String nombreCompleto = getNombreCompleto();

		return nombreCompletoParte.equals(nombreCompleto);
	}

	public String getNombreCompleto() {
		String nombreCompleto = nombre == null ? "" : nombre;
		nombreCompleto += primerApellido == null ? "" : primerApellido;
		nombreCompleto += segundoApellido == null ? "" : segundoApellido;

		return nombreCompleto;
	}

}
