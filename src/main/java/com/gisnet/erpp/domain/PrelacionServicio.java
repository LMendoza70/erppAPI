package com.gisnet.erpp.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A PrelacionServicio.
 */
@Entity
@Table(name = "prelacion_servicio")
public class PrelacionServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prelacionServicioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "prelacionServicioGenerator", sequenceName="prelacion_servicio_seq")
    private Long id;

    @OneToMany(mappedBy = "prelacionServicio")
    @JsonIgnore
    private Set<Certificado> certificadosParaPrelacionServicios = new HashSet<>();
    
    @ManyToOne(optional = false)
    @NotNull
    private Prelacion prelacion;

    @ManyToOne
    private Servicio servicio;

    @ManyToOne
    private SubServicio subServicio;

    @ManyToOne
    private SubSubServicio subSubServicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Set<Certificado> getCertificadosParaPrelacionServicios() {
		return certificadosParaPrelacionServicios;
	}

	public void setCertificadosParaPrelacionServicios(Set<Certificado> certificadosParaPrelacionServicios) {
		this.certificadosParaPrelacionServicios = certificadosParaPrelacionServicios;
	}

	public Prelacion getPrelacion() {
        return prelacion;
    }

    public PrelacionServicio prelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
        return this;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public PrelacionServicio servicio(Servicio servicio) {
        this.servicio = servicio;
        return this;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public SubServicio getSubServicio() {
        return subServicio;
    }

    public PrelacionServicio subServicio(SubServicio subServicio) {
        this.subServicio = subServicio;
        return this;
    }

    public void setSubServicio(SubServicio subServicio) {
        this.subServicio = subServicio;
    }

    public SubSubServicio getSubSubServicio() {
        return subSubServicio;
    }

    public PrelacionServicio subSubServicio(SubSubServicio subSubServicio) {
        this.subSubServicio = subSubServicio;
        return this;
    }

    public void setSubSubServicio(SubSubServicio subSubServicio) {
        this.subSubServicio = subSubServicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrelacionServicio prelacionServicio = (PrelacionServicio) o;
        if (prelacionServicio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prelacionServicio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PrelacionServicio{" +
            "id=" + getId() +
            "}";
    }
}
