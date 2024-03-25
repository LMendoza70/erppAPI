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
 * A Asentamiento.
 */
@Entity
@Table(name = "asentamiento")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Asentamiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asentamientoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "asentamientoGenerator", sequenceName="asentamiento_seq")
    private Long id;

    @Size(max = 4)
    @Column(name = "num_asentamiento", length = 4)
    private String numAsentamiento;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Size(max = 5)
    @Column(name = "cp", length = 5)
    private String cp;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "asentamiento")
    @JsonIgnore
    private Set<Direccion> direccionesParaAsentamientos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private TipoAsent tipoAsent;

    @ManyToOne(optional = false)
    @NotNull
    private Municipio municipio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumAsentamiento() {
        return numAsentamiento;
    }

    public Asentamiento numAsentamiento(String numAsentamiento) {
        this.numAsentamiento = numAsentamiento;
        return this;
    }

    public void setNumAsentamiento(String numAsentamiento) {
        this.numAsentamiento = numAsentamiento;
    }

    public String getNombre() {
        return nombre;
    }

    public Asentamiento nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCp() {
        return cp;
    }

    public Asentamiento cp(String cp) {
        this.cp = cp;
        return this;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Asentamiento activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Direccion> getDireccionesParaAsentamientos() {
        return direccionesParaAsentamientos;
    }

    public Asentamiento direccionesParaAsentamientos(Set<Direccion> direccions) {
        this.direccionesParaAsentamientos = direccions;
        return this;
    }

    public Asentamiento addDireccionesParaAsentamiento(Direccion direccion) {
        this.direccionesParaAsentamientos.add(direccion);
        direccion.setAsentamiento(this);
        return this;
    }

    public Asentamiento removeDireccionesParaAsentamiento(Direccion direccion) {
        this.direccionesParaAsentamientos.remove(direccion);
        direccion.setAsentamiento(null);
        return this;
    }

    public void setDireccionesParaAsentamientos(Set<Direccion> direccions) {
        this.direccionesParaAsentamientos = direccions;
    }

    public TipoAsent getTipoAsent() {
        return tipoAsent;
    }

    public Asentamiento tipoAsent(TipoAsent tipoAsent) {
        this.tipoAsent = tipoAsent;
        return this;
    }

    public void setTipoAsent(TipoAsent tipoAsent) {
        this.tipoAsent = tipoAsent;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public Asentamiento municipio(Municipio municipio) {
        this.municipio = municipio;
        return this;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Asentamiento asentamiento = (Asentamiento) o;
        if (asentamiento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), asentamiento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Asentamiento{" +
            "id=" + getId() +
            ", numAsentamiento='" + getNumAsentamiento() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", cp='" + getCp() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
