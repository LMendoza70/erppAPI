package com.gisnet.erpp.vo;

public class AcreditanteVO {

	private String graduacionCredito = "-";
	private String nombre;
	private String paterno;
	private String materno;

	public String getGraduacionCredito() {
		return graduacionCredito;
	}

	public void setGraduacionCredito(String graduacionCredito) {
		this.graduacionCredito = graduacionCredito;
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

    public String getNombreCompleto(){
		StringBuilder sb = new StringBuilder();
		if (getNombre()!=null){
			sb.append(getNombre());
		} 
		if (getPaterno()!=null){
			sb.append(" ").append(getPaterno());
		}
		if (getMaterno()!=null){
			sb.append(" ").append(getMaterno());
		}
		return sb.toString().toUpperCase();
	}
}
