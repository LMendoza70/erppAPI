package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Nacionalidad.
 */
@Entity
@Table(name = "nacionalidad")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Nacionalidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nacionalidadGenerator")
    @SequenceGenerator(allocationSize = 1, name = "nacionalidadGenerator", sequenceName="nacionalidad_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "nacionalidad")
    @JsonIgnore
    private Set<PredioPersona> predioPersonasParaNacionalidads = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Nacionalidad nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<PredioPersona> getPredioPersonasParaNacionalidads() {
        return predioPersonasParaNacionalidads;
    }

    public Nacionalidad predioPersonasParaNacionalidads(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaNacionalidads = predioPersonas;
        return this;
    }

    public Nacionalidad addPredioPersonasParaNacionalidad(PredioPersona predioPersona) {
        this.predioPersonasParaNacionalidads.add(predioPersona);
        predioPersona.setNacionalidad(this);
        return this;
    }

    public Nacionalidad removePredioPersonasParaNacionalidad(PredioPersona predioPersona) {
        this.predioPersonasParaNacionalidads.remove(predioPersona);
        predioPersona.setNacionalidad(null);
        return this;
    }

    public void setPredioPersonasParaNacionalidads(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaNacionalidads = predioPersonas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Nacionalidad nacionalidad = (Nacionalidad) o;
        if (nacionalidad.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), nacionalidad.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Nacionalidad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
