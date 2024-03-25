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
 * A Prioridad.
 */
@Entity
@Table(name = "prioridad")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Prioridad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prioridadGenerator")
    @SequenceGenerator(allocationSize = 1, name = "prioridadGenerator", sequenceName="prioridad_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "prioridad")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaPrioridads = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Prioridad nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Prelacion> getPrelacionesParaPrioridads() {
        return prelacionesParaPrioridads;
    }

    public Prioridad prelacionesParaPrioridads(Set<Prelacion> prelacions) {
        this.prelacionesParaPrioridads = prelacions;
        return this;
    }

    public Prioridad addPrelacionesParaPrioridad(Prelacion prelacion) {
        this.prelacionesParaPrioridads.add(prelacion);
        prelacion.setPrioridad(this);
        return this;
    }

    public Prioridad removePrelacionesParaPrioridad(Prelacion prelacion) {
        this.prelacionesParaPrioridads.remove(prelacion);
        prelacion.setPrioridad(null);
        return this;
    }

    public void setPrelacionesParaPrioridads(Set<Prelacion> prelacions) {
        this.prelacionesParaPrioridads = prelacions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prioridad prioridad = (Prioridad) o;
        if (prioridad.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prioridad.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Prioridad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
