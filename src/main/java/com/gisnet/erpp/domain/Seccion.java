package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Seccion.
 */
@Entity
@Table(name = "seccion")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Seccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seccionGenerator")
    @SequenceGenerator(allocationSize = 1, name = "seccionGenerator", sequenceName="seccion_seq")
    private Long id;

    @Size(max = 150)
    @Column(name = "nombre", length = 150)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    
    @Column(name = "clave", length = 8)
    private String clave;
	
    @Column(name = "inscripciones")
    private Boolean inscripciones;
    
    @OneToMany(mappedBy = "seccion")
    @JsonIgnore
    private Set<SeccionPorOficina> seccionesPorOficina = new HashSet<>();


    public String getNombre() {
        return nombre;
    }

    public Seccion nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
	
    public Boolean getInscripciones() {
    	return inscripciones;
    }
	
    public void setInscripciones(Boolean inscripciones) {
    	this.inscripciones = inscripciones;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Seccion activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public String getClave() {
        return clave;
    }

    public Seccion clave(String clave) {
        this.clave = clave;
        return this;
    }


	public void setClave(String clave) {
		this.clave = clave;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<SeccionPorOficina> getSeccionesPorOficina() {
		return seccionesPorOficina;
	}

	public void setSeccionesPorOficina(Set<SeccionPorOficina> seccionesPorOficina) {
		this.seccionesPorOficina = seccionesPorOficina;
	}
}
