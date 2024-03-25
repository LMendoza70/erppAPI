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
 * A TipoAsent.
 */
@Entity
@Table(name = "tipo_asent")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoAsent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoAsentGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoAsentGenerator", sequenceName="tipo_asent_seq")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "nombre", length = 30, nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "tipoAsent")
    @JsonIgnore
    private Set<Asentamiento> asentamientosParaTipoAsents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoAsent nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public TipoAsent activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Asentamiento> getAsentamientosParaTipoAsents() {
        return asentamientosParaTipoAsents;
    }

    public TipoAsent asentamientosParaTipoAsents(Set<Asentamiento> asentamientos) {
        this.asentamientosParaTipoAsents = asentamientos;
        return this;
    }

    public TipoAsent addAsentamientosParaTipoAsent(Asentamiento asentamiento) {
        this.asentamientosParaTipoAsents.add(asentamiento);
        asentamiento.setTipoAsent(this);
        return this;
    }

    public TipoAsent removeAsentamientosParaTipoAsent(Asentamiento asentamiento) {
        this.asentamientosParaTipoAsents.remove(asentamiento);
        asentamiento.setTipoAsent(null);
        return this;
    }

    public void setAsentamientosParaTipoAsents(Set<Asentamiento> asentamientos) {
        this.asentamientosParaTipoAsents = asentamientos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoAsent tipoAsent = (TipoAsent) o;
        if (tipoAsent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoAsent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoAsent{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
