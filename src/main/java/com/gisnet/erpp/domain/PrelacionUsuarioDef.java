package com.gisnet.erpp.domain;

import javax.persistence.*;
import java.io.Serializable;

import javax.persistence.*;

import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A PrelacionUsuarioDef.
 */
@Entity
@Table(name = "prelacion_usuario_def")

public class PrelacionUsuarioDef implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prelacionUsuarioDefGenerator")
    @SequenceGenerator(allocationSize = 1, name = "prelacionUsuarioDefGenerator", sequenceName="prelacion_usuario_def_seq")
    private Long id;

    @ManyToOne
    private Prelacion prelacion;
    
    @ManyToOne
    private SolicitudDevolucion solicitudDevolucion;

    @Size(max = 100)
    @Column(name = "paterno", length = 100)
    private String paterno;

    @Size(max = 80)
    @Column(name = "materno", length = 80)
    private String materno;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @Size(max = 100)
    @Column(name = "telefono", length = 100)
    private String telefono;

    @ManyToOne
    private TipoIden tipoIden;

    @Column(name = "valor_identificacion")
    private String valor;
    
    @Transient
    @JsonProperty
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
    	return sb.toString();
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoIden getTipoIden() {
        return tipoIden;
    }

    public void setTipoIden(TipoIden tipoIden) {
        this.tipoIden = tipoIden;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Prelacion getPrelacion() {
        return prelacion;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

    public String getValor() {
        return this.valor ;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }

	public SolicitudDevolucion getSolicitudDevolucion() {
		return solicitudDevolucion;
	}

	public void setSolicitudDevolucion(SolicitudDevolucion solicitudDevolucion) {
		this.solicitudDevolucion = solicitudDevolucion;
	}
}
