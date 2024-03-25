package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A EstadoCivil.
 */
@Entity
@Table(name = "estado_civil")
public class EstadoCivil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estadoCivilGenerator")
    @SequenceGenerator(allocationSize = 1, name = "estadoCivilGenerator", sequenceName="estado_civil_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "estadoCivil")
    @JsonIgnore
    private Set<PredioPersona> predioPersonasParaEstadoCivils = new HashSet<>();
    
    @ManyToOne
    private CampoValores campoValores;

    public CampoValores getCampoValores() {
		return campoValores;
	}

	public void setCampoValores(CampoValores campoValores) {
		this.campoValores = campoValores;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public EstadoCivil nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<PredioPersona> getPredioPersonasParaEstadoCivils() {
        return predioPersonasParaEstadoCivils;
    }

    public EstadoCivil predioPersonasParaEstadoCivils(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaEstadoCivils = predioPersonas;
        return this;
    }

    public EstadoCivil addPredioPersonasParaEstadoCivil(PredioPersona predioPersona) {
        this.predioPersonasParaEstadoCivils.add(predioPersona);
        predioPersona.setEstadoCivil(this);
        return this;
    }

    public EstadoCivil removePredioPersonasParaEstadoCivil(PredioPersona predioPersona) {
        this.predioPersonasParaEstadoCivils.remove(predioPersona);
        predioPersona.setEstadoCivil(null);
        return this;
    }

    public void setPredioPersonasParaEstadoCivils(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaEstadoCivils = predioPersonas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EstadoCivil estadoCivil = (EstadoCivil) o;
        if (estadoCivil.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), estadoCivil.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EstadoCivil{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
