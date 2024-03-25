package com.gisnet.erpp.web.api.account;

import javax.validation.constraints.Size;

import com.gisnet.erpp.domain.Estado;
import com.gisnet.erpp.domain.Municipio;
import com.gisnet.erpp.domain.Usuario;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

public class PostRegistroDTO  {
	private String password; 
	
    private String nombre;
    
    private String paterno;
    
    private String materno;

    private String rfc;
    
    private String curp;
    
    private Date fechaNac;
    
    private String numTelefono;
    
    private String token;
    
    private Long tipoUsuarioId;
    
    private String calle;
    
    private String numExt;
    
    private String numInt;
    
    private String colonia;
    
    @Size(min = 5, max = 5)
    private String cp;   
    
    private Estado estado;
    
    private Municipio municipio;
    
	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
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




	public String getRfc() {
		return rfc;
	}




	public void setRfc(String rfc) {
		this.rfc = rfc;
	}




	public String getCurp() {
		return curp;
	}




	public void setCurp(String curp) {
		this.curp = curp;
	}




	public Date getFechaNac() {
		return fechaNac;
	}




	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}




	public String getNumTelefono() {
		return numTelefono;
	}




	public void setNumTelefono(String numTelefono) {
		this.numTelefono = numTelefono;
	}




	public String getToken() {
		return token;
	}




	public void setToken(String token) {
		this.token = token;
	}




	public Long getTipoUsuarioId() {
		return tipoUsuarioId;
	}




	public void setTipoUsuarioId(Long tipoUsuarioId) {
		this.tipoUsuarioId = tipoUsuarioId;
	}




	public String getCalle() {
		return calle;
	}




	public void setCalle(String calle) {
		this.calle = calle;
	}




	public String getNumExt() {
		return numExt;
	}




	public void setNumExt(String numExt) {
		this.numExt = numExt;
	}




	public String getNumInt() {
		return numInt;
	}




	public void setNumInt(String numInt) {
		this.numInt = numInt;
	}




	public String getColonia() {
		return colonia;
	}




	public void setColonia(String colonia) {
		this.colonia = colonia;
	}




	public String getCp() {
		return cp;
	}




	public void setCp(String cp) {
		this.cp = cp;
	}
	
	




	



	public Estado getEstado() {
		return estado;
	}




	public void setEstado(Estado estado) {
		this.estado = estado;
	}




	public Municipio getMunicipio() {
		return municipio;
	}




	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}




	@Override
    public String toString() {
		StringBuilder sb = new StringBuilder("Registro=");
		sb.append("token=").append(getToken());
		sb.append("nombre=").append(getNombre());
		sb.append("paterno=").append(getPaterno());
		sb.append("materno=").append(getMaterno());
		sb.append("tipoUsuarioId=").append(getTipoUsuarioId());
		if (estado!=null){
			sb.append("estado=").append(getEstado().getNombre());
		}
		
		if (municipio!=null){
			sb.append("municipio=").append(getMunicipio().getNombre());
		}

        return sb.toString();
    }
}
