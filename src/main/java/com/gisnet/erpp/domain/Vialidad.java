package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Vialidad.
 */
@Entity
@Table(name = "vialidad")
public class Vialidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vialidadGenerator")
    @SequenceGenerator(allocationSize = 1, name = "vialidadGenerator", sequenceName="vialidad_seq")
    private Long id;

    @Size(max = 5)
    @Column(name = "num_vialidad", length = 5)
    private String numVialidad;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "vialidad")
    @JsonIgnore
    private Set<Colindancia> colindanciasParaVialidads = new HashSet<>();

    @OneToMany(mappedBy = "vialidadEntre2")
    @JsonIgnore
    private Set<Direccion> direccionesParaVialidadEntre2S = new HashSet<>();

    @OneToMany(mappedBy = "vialidadEntre1")
    @JsonIgnore
    private Set<Direccion> direccionesParaVialidadEntre1S = new HashSet<>();

    @OneToMany(mappedBy = "vialidad")
    @JsonIgnore
    private Set<Direccion> direccionesParaVialidads = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Municipio municipio;

    @ManyToOne(optional = false)
    @NotNull
    private TipoVialidad tipoVialidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumVialidad() {
        return numVialidad;
    }

    public Vialidad numVialidad(String numVialidad) {
        this.numVialidad = numVialidad;
        return this;
    }

    public void setNumVialidad(String numVialidad) {
        this.numVialidad = numVialidad;
    }

    public String getNombre() {
        return nombre;
    }

    public Vialidad nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Vialidad activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Colindancia> getColindanciasParaVialidads() {
        return colindanciasParaVialidads;
    }

    public Vialidad colindanciasParaVialidads(Set<Colindancia> colindancias) {
        this.colindanciasParaVialidads = colindancias;
        return this;
    }

    public Vialidad addColindanciasParaVialidad(Colindancia colindancia) {
        this.colindanciasParaVialidads.add(colindancia);
        colindancia.setVialidad(this);
        return this;
    }

    public Vialidad removeColindanciasParaVialidad(Colindancia colindancia) {
        this.colindanciasParaVialidads.remove(colindancia);
        colindancia.setVialidad(null);
        return this;
    }

    public void setColindanciasParaVialidads(Set<Colindancia> colindancias) {
        this.colindanciasParaVialidads = colindancias;
    }

    public Set<Direccion> getDireccionesParaVialidadEntre2S() {
        return direccionesParaVialidadEntre2S;
    }

    public Vialidad direccionesParaVialidadEntre2S(Set<Direccion> direccions) {
        this.direccionesParaVialidadEntre2S = direccions;
        return this;
    }

    public Vialidad addDireccionesParaVialidadEntre2(Direccion direccion) {
        this.direccionesParaVialidadEntre2S.add(direccion);
        direccion.setVialidadEntre2(this);
        return this;
    }

    public Vialidad removeDireccionesParaVialidadEntre2(Direccion direccion) {
        this.direccionesParaVialidadEntre2S.remove(direccion);
        direccion.setVialidadEntre2(null);
        return this;
    }

    public void setDireccionesParaVialidadEntre2S(Set<Direccion> direccions) {
        this.direccionesParaVialidadEntre2S = direccions;
    }

    public Set<Direccion> getDireccionesParaVialidadEntre1S() {
        return direccionesParaVialidadEntre1S;
    }

    public Vialidad direccionesParaVialidadEntre1S(Set<Direccion> direccions) {
        this.direccionesParaVialidadEntre1S = direccions;
        return this;
    }

    public Vialidad addDireccionesParaVialidadEntre1(Direccion direccion) {
        this.direccionesParaVialidadEntre1S.add(direccion);
        direccion.setVialidadEntre1(this);
        return this;
    }

    public Vialidad removeDireccionesParaVialidadEntre1(Direccion direccion) {
        this.direccionesParaVialidadEntre1S.remove(direccion);
        direccion.setVialidadEntre1(null);
        return this;
    }

    public void setDireccionesParaVialidadEntre1S(Set<Direccion> direccions) {
        this.direccionesParaVialidadEntre1S = direccions;
    }

    public Set<Direccion> getDireccionesParaVialidads() {
        return direccionesParaVialidads;
    }

    public Vialidad direccionesParaVialidads(Set<Direccion> direccions) {
        this.direccionesParaVialidads = direccions;
        return this;
    }

    public Vialidad addDireccionesParaVialidad(Direccion direccion) {
        this.direccionesParaVialidads.add(direccion);
        direccion.setVialidad(this);
        return this;
    }

    public Vialidad removeDireccionesParaVialidad(Direccion direccion) {
        this.direccionesParaVialidads.remove(direccion);
        direccion.setVialidad(null);
        return this;
    }

    public void setDireccionesParaVialidads(Set<Direccion> direccions) {
        this.direccionesParaVialidads = direccions;
    }


	public Municipio getMunicipio() {
        return municipio;
    }

    public Vialidad municipio(Municipio municipio) {
        this.municipio = municipio;
        return this;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public TipoVialidad getTipoVialidad() {
        return tipoVialidad;
    }

    public Vialidad tipoVialidad(TipoVialidad tipoVialidad) {
        this.tipoVialidad = tipoVialidad;
        return this;
    }

    public void setTipoVialidad(TipoVialidad tipoVialidad) {
        this.tipoVialidad = tipoVialidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vialidad vialidad = (Vialidad) o;
        if (vialidad.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vialidad.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Vialidad{" +
            "id=" + getId() +
            ", numVialidad='" + getNumVialidad() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
