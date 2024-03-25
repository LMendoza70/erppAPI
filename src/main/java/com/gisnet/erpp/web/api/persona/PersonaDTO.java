package com.gisnet.erpp.web.api.persona;



import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Telefono;
import com.gisnet.erpp.domain.TipoPersona;
import com.gisnet.erpp.domain.Direccion;
import com.gisnet.erpp.domain.Identificacion;
import com.gisnet.erpp.domain.Nacionalidad;


public class PersonaDTO {
	
	private Long id;
	
	private Boolean publica;

    private String paterno;

    private String materno;

    private String nombre;

    private Date fechaNac;

    private String rfc;

    private String curp;

    private String email;

    private Boolean activo;

    private Integer tipoHistorico;

    private String hashFila;

    private Integer nuTit;
    
    private TipoPersona tipoPersona;
    
    private Nacionalidad nacionalidad;
	
	private Set<Telefono> telefonosParaPersonas =  new HashSet<>();
	
	private Set<Direccion> direccionesParaPersonas =  new HashSet<>();
	
	private Set<Identificacion> identificacionesParaPersonas =  new HashSet<>();
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public PersonaDTO() {
		
	}
	
	public PersonaDTO(Persona persona) {
		
		this.id = persona.getId();
		this.publica = persona.isPublica();
		this.paterno = persona.getPaterno();
		this.materno = persona.getMaterno();
		this.nombre = persona.getNombre();
		this.fechaNac =  persona.getFechaNac();
		this.rfc = persona.getRfc();
		this.curp = persona.getCurp();
		this.email = persona.getEmail();
		this.activo = persona.isActivo();
		this.tipoHistorico = persona.getTipoHistorico();
		this.hashFila = persona.getHashFila();
		this.nuTit = persona.getNuTit();
		this.tipoPersona = persona.getTipoPersona();
		this.nacionalidad = persona.getNacionalidad();
		persona.getIdentificacionesParaPersonas().forEach(x -> this.identificacionesParaPersonas.add(x));
		persona.getTelefonosParaPersonas().forEach(x -> this.telefonosParaPersonas.add(x));
		persona.getDireccionesParaPersonas().forEach(x -> this.direccionesParaPersonas.add(x));
		
	}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getNuTit() {
        return nuTit;
    }
    
    public void setNuTit(Integer nuTit){
        this.nuTit = nuTit;
    }
    
    public Integer getTipoHistorico() {
        return tipoHistorico;
    }
    
    public void setTipoHistorico(Integer tipoHistorico){
        this.tipoHistorico = tipoHistorico;
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
    
    public void setPaterno(String paterno) {
        this.paterno = paterno.toUpperCase();
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno.toUpperCase();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
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

    public void setRfc(String rfc) {
        this.rfc = rfc.toUpperCase();
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp.toUpperCase();
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

    public Set<Direccion> getDireccionesParaPersonas() {
        return direccionesParaPersonas;
    }

    public void setDireccionesParaPersonas(Set<Direccion> direcciones) {
        this.direccionesParaPersonas = direcciones;
    }

    public Set<Identificacion> getIdentificacionesParaPersonas() {
        return identificacionesParaPersonas;
    }

    public void setIdentificacionesParaPersonas(Set<Identificacion> identificaciones) {
        this.identificacionesParaPersonas = identificaciones;
    }

    public Set<Telefono> getTelefonosParaPersonas() {
        return telefonosParaPersonas;
    }

    public void setTelefonosParaPersonas(Set<Telefono> telefonos) {
        this.telefonosParaPersonas = telefonos;
    }
    
    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }
    
    public Nacionalidad getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    @Override
    public String toString() {
        return "Persona{" +
            "id=" + getId() +
            ", publica='" + isPublica() + "'" +
            ", paterno='" + getPaterno() + "'" +
            ", materno='" + getMaterno() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", fechaNac='" + getFechaNac() + "'" +
            ", rfc='" + getRfc() + "'" +
            ", curp='" + getCurp() + "'" +
            ", email='" + getEmail() + "'" +
            ", activo='" + isActivo() + "'" +
            ", hashFila='" + getHashFila() + "'" +
            "}";
    }
}
