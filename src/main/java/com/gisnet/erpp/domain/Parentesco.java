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
 * A Parentesco.
 */
@Entity
@Table(name = "parentesco")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Parentesco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parentescoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "parentescoGenerator", sequenceName="parentesco_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "parentesco")
    @JsonIgnore
    private Set<PredioPersona> predioPersonasParaParentescos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Parentesco nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<PredioPersona> getPredioPersonasParaParentescos() {
        return predioPersonasParaParentescos;
    }

    public Parentesco predioPersonasParaParentescos(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaParentescos = predioPersonas;
        return this;
    }

    public Parentesco addPredioPersonasParaParentesco(PredioPersona predioPersona) {
        this.predioPersonasParaParentescos.add(predioPersona);
        predioPersona.setParentesco(this);
        return this;
    }

    public Parentesco removePredioPersonasParaParentesco(PredioPersona predioPersona) {
        this.predioPersonasParaParentescos.remove(predioPersona);
        predioPersona.setParentesco(null);
        return this;
    }

    public void setPredioPersonasParaParentescos(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaParentescos = predioPersonas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Parentesco parentesco = (Parentesco) o;
        if (parentesco.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), parentesco.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Parentesco{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
