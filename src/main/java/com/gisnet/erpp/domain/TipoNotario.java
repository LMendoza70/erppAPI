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
 * A TipoNotario.
 */
@Entity
@Table(name = "tipo_notario")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoNotario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoNotarioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoNotarioGenerator", sequenceName="tipo_notario_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "tipoNotario")
    @JsonIgnore
    private Set<Notario> notariosParaTipoNotarios = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoNotario nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Notario> getNotariosParaTipoNotarios() {
        return notariosParaTipoNotarios;
    }

    public TipoNotario notariosParaTipoNotarios(Set<Notario> notarios) {
        this.notariosParaTipoNotarios = notarios;
        return this;
    }

    public TipoNotario addNotariosParaTipoNotario(Notario notario) {
        this.notariosParaTipoNotarios.add(notario);
        notario.setTipoNotario(this);
        return this;
    }

    public TipoNotario removeNotariosParaTipoNotario(Notario notario) {
        this.notariosParaTipoNotarios.remove(notario);
        notario.setTipoNotario(null);
        return this;
    }

    public void setNotariosParaTipoNotarios(Set<Notario> notarios) {
        this.notariosParaTipoNotarios = notarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoNotario tipoNotario = (TipoNotario) o;
        if (tipoNotario.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoNotario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoNotario{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
