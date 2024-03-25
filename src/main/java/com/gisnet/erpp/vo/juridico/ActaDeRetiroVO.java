package com.gisnet.erpp.vo.juridico;

import java.util.Date;

public class ActaDeRetiroVO {
	
	private String horaNumero;
	private String horaLetra;
	private String dia;
	private String mesLetra;
	private String nombreDirector;
	private String ciudadano;
	private String folioCredencial;
	private String diaDeposito;
	private String mesLetraDeposito;
	private Long numeroActa;
	private Long libro;
	private String registradorAsignado;

	public String getHoraNumero(){
		return this.horaNumero;
	}
	
	public void setHoraNumero(String horaNumero){
		this.horaNumero = horaNumero;
	}

	public String getHoraLetra(){
		return this.horaLetra;
	}
	
	public void setHoraLetra(String horaLetra){
		this.horaLetra = horaLetra;
	}

	public String getDia(){
		return this.dia;
	}
	
	public void setDia(String dia){
		this.dia = dia;
	}

	public String getMesLetra(){
		return this.mesLetra;
	}
	
	public void setMesLetra(String mesLetra){
		this.mesLetra = mesLetra;
	}

	public String getNombreDirector(){
		return this.nombreDirector;
	}
	
	public void setNombreDirector(String nombreDirector){
		this.nombreDirector = nombreDirector;
	}

	 public String getCiudadano(){
	  return this.ciudadano;
	 }
	 
	 public void setCiudadano(String ciudadano){
	  this.ciudadano = ciudadano;
	 }

	public String getFolioCredencial(){
		return this.folioCredencial;
	}
	
	public void setFolioCredencial(String folioCredencial){
		this.folioCredencial = folioCredencial;
	}

	 public String getDiaDeposito(){
	  return this.diaDeposito;
	 }
	 
	 public void setDiaDeposito(String diaDeposito){
	  this.diaDeposito = diaDeposito;
	 }

	  public String getMesLetraDeposito(){
	   return this.mesLetraDeposito;
	  }
	  
	  public void setMesLetraDeposito(String mesLetraDeposito){
	   this.mesLetraDeposito = mesLetraDeposito;
	  }


	public Long getNumeroActa(){
		return this.numeroActa;
	}
	
	public void setNumeroActa(Long numeroActa){
		this.numeroActa = numeroActa;
	}
	public Long getLibro(){
		return this.libro;
	}
	
	public void setLibro(Long libro){
		this.libro = libro;
	}	

	public String getRegistradorAsignado(){
		return this.registradorAsignado;
	}
	
	public void setRegistradorAsignado(String registradorAsignado){
		this.registradorAsignado = registradorAsignado;
	}

}