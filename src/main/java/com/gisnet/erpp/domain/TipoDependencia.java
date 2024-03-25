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
@Table(name = "tipo_dependencia")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoDependencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoDependenciaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoDependenciaGenerator", sequenceName="tipo_dependencia_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "tipoDependencia")
    @JsonIgnore
    private Set<Dependencia> dependenciasParaTipoDependencias = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoDependencia nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Dependencia> getDependenciasParaTipoDependencias() {
        return dependenciasParaTipoDependencias;
    }

    public TipoDependencia DependenciasParaTipoDependencias(Set<Dependencia> dependencias) {
        this.dependenciasParaTipoDependencias = dependencias;
        return this;
    }

    public TipoDependencia addDependenciasParaTipoDependencia(Dependencia dependencia) {
        this.dependenciasParaTipoDependencias.add(dependencia);
        dependencia.setTipoDependencia(this);
        return this;
    }

    public TipoDependencia removeDependenciasParaTipoDependencia(Dependencia dependencia) {
        this.dependenciasParaTipoDependencias.remove(dependencia);
        dependencia.setTipoDependencia(null);
        return this;
    }

    public void setDependenciasParaTipoDependencias(Set<Dependencia> dependencias) {
        this.dependenciasParaTipoDependencias = dependencias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoDependencia tipoNotario = (TipoDependencia) o;
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
        return "TipoDependencia{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
