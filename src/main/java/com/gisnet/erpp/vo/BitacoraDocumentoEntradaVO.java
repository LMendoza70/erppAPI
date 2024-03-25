package com.gisnet.erpp.vo;

import java.util.Date;
import com.gisnet.erpp.domain.Archivo;

public class BitacoraDocumentoEntradaVO {

    private Date fecha;
    private String accion;
    private String nomUsuario;
    private String nomDocumento;
    private Archivo archivo;
    private String observaciones;
    private Boolean esAnexo;
    
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public String getAccion() {
		return accion;
	}
	
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	public String getNomUsuario() {
		return nomUsuario;
	}
	
	public void setNomUsuario(String nomUsuario) {
		this.nomUsuario = nomUsuario;
	}
	
	public String getNomDocumento() {
		return nomDocumento;
	}
	
	public void setNomDocumento(String nomDocumento) {
		this.nomDocumento = nomDocumento;
	}
	
	public Archivo getArchivo() {
		return archivo;
	}
	
	public void setArchivo(Archivo archivo) {
		this.archivo = archivo;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Boolean getEsAnexo() {
		return esAnexo;
	}

	public void setEsAnexo(Boolean esAnexo) {
		this.esAnexo = esAnexo;
	}
	
	

}