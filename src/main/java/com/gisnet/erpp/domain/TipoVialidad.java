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
 * A TipoVialidad.
 */
@Entity
@Table(name = "tipo_vialidad")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoVialidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoVialidadGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoVialidadGenerator", sequenceName="tipo_vialidad_seq")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "tipoVialidad")
    @JsonIgnore
    private Set<Vialidad> vialidadesParaTipoVialidads = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoVialidad nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public TipoVialidad activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Vialidad> getVialidadesParaTipoVialidads() {
        return vialidadesParaTipoVialidads;
    }

    public TipoVialidad vialidadesParaTipoVialidads(Set<Vialidad> vialidads) {
        this.vialidadesParaTipoVialidads = vialidads;
        return this;
    }

    public TipoVialidad addVialidadesParaTipoVialidad(Vialidad vialidad) {
        this.vialidadesParaTipoVialidads.add(vialidad);
        vialidad.setTipoVialidad(this);
        return this;
    }

    public TipoVialidad removeVialidadesParaTipoVialidad(Vialidad vialidad) {
        this.vialidadesParaTipoVialidads.remove(vialidad);
        vialidad.setTipoVialidad(null);
        return this;
    }

    public void setVialidadesParaTipoVialidads(Set<Vialidad> vialidads) {
        this.vialidadesParaTipoVialidads = vialidads;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoVialidad tipoVialidad = (TipoVialidad) o;
        if (tipoVialidad.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoVialidad.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoVialidad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
