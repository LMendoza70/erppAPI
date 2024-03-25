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
@Table(name = "ambito")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ambito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ambitoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "ambitoGenerator", sequenceName="ambito_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "ambito")
    @JsonIgnore
    private Set<Dependencia> dependenciasParaAmbitos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Ambito nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Dependencia> getDependenciasParaAmbitos() {
        return dependenciasParaAmbitos;
    }

    public Ambito dependenciasParaAmbitos(Set<Dependencia> dependencias) {
        this.dependenciasParaAmbitos = dependencias;
        return this;
    }

    public Ambito addDependenciaParaAmbito(Dependencia dependencia) {
        this.dependenciasParaAmbitos.add(dependencia);
        dependencia.setAmbito(this);
        return this;
    }

    public Ambito removeDependenciaParaAmbito(Dependencia dependencia) {
        this.dependenciasParaAmbitos.remove(dependencia);
        dependencia.setAmbito(null);
        return this;
    }

    public void setDependenciasParaAmbitos(Set<Dependencia> dependencias) {
        this.dependenciasParaAmbitos = dependencias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ambito tipoNotario = (Ambito) o;
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
