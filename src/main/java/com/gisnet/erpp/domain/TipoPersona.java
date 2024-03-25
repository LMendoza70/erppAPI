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
 * A TipoPersona.
 */
@Entity
@Table(name = "tipo_persona")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class TipoPersona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoPersonaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoPersonaGenerator", sequenceName="tipo_persona_seq")
    private Long id;

    @Size(max = 20)
    @Column(name = "nombre", length = 20)
    private String nombre;

    @OneToMany(mappedBy = "tipoPersona")
    @JsonIgnore
    private Set<Persona> personasParaTipoPersonas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoPersona nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Persona> getPersonasParaTipoPersonas() {
        return personasParaTipoPersonas;
    }

    public TipoPersona personasParaTipoPersonas(Set<Persona> personas) {
        this.personasParaTipoPersonas = personas;
        return this;
    }

    public TipoPersona addPersonasParaTipoPersona(Persona persona) {
        this.personasParaTipoPersonas.add(persona);
        persona.setTipoPersona(this);
        return this;
    }

    public TipoPersona removePersonasParaTipoPersona(Persona persona) {
        this.personasParaTipoPersonas.remove(persona);
        persona.setTipoPersona(null);
        return this;
    }

    public void setPersonasParaTipoPersonas(Set<Persona> personas) {
        this.personasParaTipoPersonas = personas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoPersona tipoPersona = (TipoPersona) o;
        if (tipoPersona.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoPersona.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoPersona{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
