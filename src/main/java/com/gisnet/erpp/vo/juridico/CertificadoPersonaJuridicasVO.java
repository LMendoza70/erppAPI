package com.gisnet.erpp.vo.juridico;

import java.util.Date;
import java.util.List;

public class CertificadoPersonaJuridicasVO {
	private String  fechaIngreso;
	private String  horaIngreso;
	private Integer noPrelacion;
	private String datosRegistro;
	private String sociedad;
	private String solicitud;
	private String ciudad;
	private String horas;
	private String dia;
	private String mesLetra;
	private String anio;
	private String noRecibo;
	private String total;
	private String reviso;
	private String autorizo;
	private String cargoAutorizo;
	private String busqueda;
	private String firma;
	private String firmaAutorizo;



	public String getFirma(){
		return firma;
	}

	public void setFirma(String firma){
		this.firma = firma;
	}

	public String getFirmaAutorizo(){
		return autorizo;
	}

	public void setFirmaAutorizo(String firmaAutorizo){
		this.firmaAutorizo = firmaAutorizo;
	}


	public String getBusqueda(){
	  return busqueda;
	 }
	 
	 public void setBusqueda(String busqueda){
	  this.busqueda = busqueda;
	 }

	public String getCargoAutorizo(){
		return cargoAutorizo;
	}

	public void setCargoAutorizo(String cargoAutorizo){
		this.cargoAutorizo = cargoAutorizo;
	}

	public String getAutorizo(){
		return autorizo;
	}

	public void setAutorizo(String autorizo){
		this.autorizo = autorizo;
	}

	public String getReviso(){
		return reviso;
	}

	public void setReviso(String reviso){
		this.reviso = reviso;
	}

	public String getTotal(){
		return total;
	}

	public void setTotal(String total){
		this.total = total;
	}

	public String getNoRecibo(){
		return noRecibo;
	}

	public void setNoRecibo(String noRecibo){
		this.noRecibo = noRecibo;
	}


	public String getAnio(){
		return anio;
	}

	public void setAnio(String anio){
		this.anio = anio;
	}

	public String getMesLetra(){
		return mesLetra;
	}

	public void setMesLetra(String mesLetra){
		this.mesLetra = mesLetra;
	}

	public String getDia(){
		return dia;
	}

	public void setDia(String dia){
		this.dia = dia;
	}

	public String getHoras(){
		return horas;
	}

	public void setHoras(String horas){
		this.horas = horas;
	}

	public String getCiudad(){
		return ciudad;
	}

	public void setCiudad(String ciudad){
		this.ciudad = ciudad;
	}

	public String getSolicitud(){
		return solicitud;
	}

	public void setSolicitud(String solicitud){
		this.solicitud = solicitud;
	}

	public String getSociedad(){
		return sociedad;
	}

	public void setSociedad(String sociedad){
		this.sociedad = sociedad;
	}

	public String getDatosRegistro(){
		return datosRegistro;
	}

	public void setDatosRegistro(String datosRegistro){
		this.datosRegistro = datosRegistro;
	}

	public Integer getNoPrelacion(){
		return noPrelacion;
	}

	public void setNoPrelacion(Integer noPrelacion){
		this.noPrelacion = noPrelacion;
	}
	

	public String getFechaIngreso(){
		return fechaIngreso;
	}

	public void setFechaIngreso(String fechaIngreso){
		this.fechaIngreso = fechaIngreso;
	}

	public String getHoraIngreso(){
		return horaIngreso;
	}

	public void setHoraIngreso(String horaIngreso){
		this.horaIngreso = horaIngreso;
	}
	



}