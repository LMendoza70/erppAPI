package com.gisnet.erpp.vo;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;


import com.gisnet.erpp.domain.Direccion;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Identificacion;
import com.gisnet.erpp.domain.Telefono;
import com.gisnet.erpp.domain.TipoPersona;
import com.gisnet.erpp.domain.Nacionalidad;

public class PersonaVO {

	private Long id;
	private TipoPersona tipoPersona;
	private Boolean publica = null;
	private String paterno = "";
	private String materno = "";
	private String nombre = "";
	private Date fechaNac = null;
	private String rfc = "";
	private String curp = "";
	private String email = "";
	private Boolean activo = false;
	private String hashFila = "";
	private Set<Direccion> direccionesParaPersonas = new HashSet();
	private Set<Identificacion> identificacionesParaPersonas = new HashSet();
	private Set<Telefono> telefonosParaPersonas = new HashSet();
	private Nacionalidad nacionalidad; 

	
		public Long getId() {
			return id;
	}

	public void setId(Long id) {
			this.id = id;
	}

	public Boolean isPublica() {
			return publica;
	}

	public void setPublica(Boolean publica) {
			this.publica = publica;
	}

	public String getPaterno() {
			return paterno;
	}

	public void setPaterno(String val) {
				this.paterno = val ==null? "": val.toUpperCase();
	}

	public String getMaterno() {
			return materno;
	}

	public void setMaterno(String val) {
				this.materno = val ==null? "": val.toUpperCase();
	}

	public String getNombre() {
			return nombre;
	}

	public void setNombre(String val) {
			this.nombre = val ==null? "": val.toUpperCase();
	}

	public Date getFechaNac() {
			return fechaNac;
	}

	public void setFechaNac(Date fechaNac) {
			this.fechaNac = fechaNac;
	}

	public String getRfc() {
			return rfc;
	}

	public void setRfc(String val) {
			this.rfc = val ==null? "": val.toUpperCase();
	}

	public String getCurp() {
			return curp;
	}

	public void setCurp(String val) {
			this.curp = val ==null? "": val.toUpperCase();
	}

	public String getEmail() {
			return email;
	}

	public void setEmail(String email) {
			this.email = email;
	}

	public Boolean isActivo() {
			return activo;
	}

	public void setActivo(Boolean activo) {
			this.activo = activo;
	}

	public String getHashFila() {
			return hashFila;
	}

	public void setHashFila(String hashFila) {
			this.hashFila = hashFila;
	}
	
	
	public void setDireccionesParaPersonas(Direccion[] direcciones){
    	this.direccionesParaPersonas = new HashSet<Direccion>();
		for(Direccion direccion : direcciones ) {
			direccion.setActivo(true); 
			this.direccionesParaPersonas.add(direccion);
		}
    }

    public  Set<Direccion> getDireccionesParaPersonas(){
        return this.direccionesParaPersonas;
    }
    
    public void setIdentificacionesParaPersonas(Identificacion[] identificaciones){
    	this.identificacionesParaPersonas = new HashSet<Identificacion>();
		for(Identificacion identificacion : identificaciones) {
			this.identificacionesParaPersonas.add(identificacion);
		}
    }

    public  Set<Identificacion> getIdentificacionesParaPersonas(){
        return this.identificacionesParaPersonas;
    }
    
    public void setTelefonosParaPersonas(Telefono[] telefonos){
    	this.telefonosParaPersonas = new HashSet<Telefono>();
		for(Telefono telefono : telefonos) {
			this.telefonosParaPersonas.add(telefono);
		}
    }

    public  Set<Telefono> getTelefonosParaPersonas(){
        return this.telefonosParaPersonas;
    }
    
    public void  setNacionalidad(Nacionalidad nacionalidad){
        this.nacionalidad = nacionalidad;
    }

    public Nacionalidad getNacionalidad(){
    	return this.nacionalidad;
    }

    public void  setTipoPersona(TipoPersona tipoPersona){
        this.tipoPersona = tipoPersona;
    }
    
    public TipoPersona getTipoPersona(){
    	return this.tipoPersona;
    }
    
    public Persona transformIntoPersona() {
    	
    	Persona persona = new Persona();
    	persona.setId(this.getId());
    	persona.setTipoPersona(this.getTipoPersona());
    	persona.setNacionalidad(this.getNacionalidad());
    	persona.setPublica(this.isPublica());
    	if(this.getPaterno() != null ) {
				persona.setPaterno(this.getPaterno());
		}

    	if(this.getMaterno() != null ) {
				persona.setMaterno(this.getMaterno());
		}
    	persona.setNombre(this.getNombre());
    	persona.setFechaNac(this.getFechaNac());
    	persona.setRfc(this.getRfc());
    	if(this.getCurp() != null ) {
				persona.setCurp(this.getCurp());
		}
    	persona.setEmail(this.getEmail());
    	persona.setActivo(this.isActivo());
    	persona.hashFila(this.getHashFila());
    	persona.setDireccionesParaPersonas(this.getDireccionesParaPersonas());
    	persona.setIdentificacionesParaPersonas(this.getIdentificacionesParaPersonas());
    	persona.setTelefonosParaPersonas(this.getTelefonosParaPersonas());
    	return persona;
    }

}