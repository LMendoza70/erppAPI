package com.gisnet.erpp.vo.juridico;

import java.util.Date;

public class ActaDeRevocacionVO {
	private Long numeroActa;
	private Long libro;	
	private String nombreTesteador;	
	private String ciudad;
	private String horaNumero;
	private String horaLetra;
	private String dia;
	private String mesLetra;
	private String anio;
	private String nombreDirector;
	private String diaRecepcion;
	private String mesRecepcion;
	private String noPrelacion;
	private String nombreNotario;
	private String ciudadano;
	private String diaTestamento;
	private String mesLetraTestamento;	
	private String registradorAsignado;
	

	public Long getNumeroActa(){
		return numeroActa;
	}
	
	public void setNumeroActa(Long numeroActa){
		this.numeroActa = numeroActa;
	}
	public Long getLibro(){
		return libro;
	}
	
	public void setLibro(Long libro){
		this.libro = libro;
	}	

	public String getNombreTesteador(){
		return nombreTesteador;
	}
	
	public void setNombreTesteador(String nombreTesteador){
		this.nombreTesteador = nombreTesteador;
	}

	public String getCiudad(){
		return ciudad;
	}
	
	public void setCiudad(String ciudad){
		this.ciudad = ciudad;
	}

	public String getHoraNumero(){
		return horaNumero;
	}
	
	public void setHoraNumero(String horaNumero){
		this.horaNumero = horaNumero;
	}

	public String getHoraLetra(){
		return horaLetra;
	}
	
	public void setHoraLetra(String horaLetra){
		this.horaLetra = horaLetra;
	}

	public String getDia(){
		return dia;
	}
	
	public void setDia(String dia){
		this.dia = dia;
	}

	public String getMesLetra(){
		return mesLetra;
	}
	
	public void setMesLetra(String mesLetra){
		this.mesLetra = mesLetra;
	}

	public String getAnio(){
		return anio;
	}
	
	public void setAnio(String anio){
		this.anio = anio;
	}

	public String getNombreDirector(){
		return nombreDirector;
	}
	
	public void setNombreDirector(String nombreDirector){
		this.nombreDirector = nombreDirector;
	}

	public String getCiudadano(){
		return ciudadano;
	}

	public void setCiudadano(String ciudadano){
		this.ciudadano = ciudadano;
	}

	public String getDiaRecepcion(){
		return diaRecepcion;
	}

	public void setDiaRecepcion(String diaRecepcion){
		this.diaRecepcion = diaRecepcion;
	}

	public String getMesRecepcion(){
		return mesRecepcion;
	}

	public void setMesRecepcion(String mesRecepcion){
		this.mesRecepcion = mesRecepcion;
	}

	public String getNoPrelacion(){
		return noPrelacion;
	}

	public void setNoPrelacion(String noPrelacion){
		this.noPrelacion = noPrelacion;
	}

	public String getNombreNotario(){
		return nombreNotario;
	}

	public void setNombreNotario(String nombreNotario){
		this.nombreNotario = nombreNotario;
	}

	public String getDiaTestamento(){
		return diaTestamento;
	}

	public void setDiaTestamento(String diaTestamento){
		this.diaTestamento = diaTestamento;
	}

	public String getMesLetraTestamento(){
		return mesLetraTestamento;
	}

	public void setMesLetraTestamento(String mesLetraTestamento){
		this.mesLetraTestamento = mesLetraTestamento;
	}

	public String getRegistradorAsignado(){
		return registradorAsignado;
	}
	
	public void setRegistradorAsignado(String registradorAsignado){
		this.registradorAsignado = registradorAsignado;
	}



}