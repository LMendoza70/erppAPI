package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "clasificacion_concepto")
public class ClasificacionConcepto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clasificacionConceptoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "clasificacionConceptoGenerator", sequenceName="clasificacion_concepto_seq")
    private Long id;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "nombre", length = 200)
    private String nombre;
    
    @OneToMany(mappedBy = "clasificacionConcepto")
    @JsonIgnore
    private Set<ServicioClasifConcepto> serviciosClasificacionesConcepto = new HashSet<>();

    public Set<ServicioClasifConcepto> getServiciosClasificacionesConcepto() {
		return serviciosClasificacionesConcepto;
	}

	public void setServiciosClasificacionesConcepto(Set<ServicioClasifConcepto> serviciosClasificacionesConcepto) {
		this.serviciosClasificacionesConcepto = serviciosClasificacionesConcepto;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClasificacionConcepto{" +
            "id=" + getId() +
            "}";
    }
}
