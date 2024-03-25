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
 * A UsoSuelo.
 */
@Entity
@Table(name = "uso_suelo")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UsoSuelo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usoSueloGenerator")
    @SequenceGenerator(allocationSize = 1, name = "usoSueloGenerator", sequenceName="uso_suelo_seq")
    private Long id;

    @Size(max = 50)
    @Column(name = "nombre", length = 50)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "usoSuelo")
    @JsonIgnore
    private Set<Predio> prediosParaUsoSuelos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public UsoSuelo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public UsoSuelo activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Predio> getPrediosParaUsoSuelos() {
        return prediosParaUsoSuelos;
    }

    public UsoSuelo prediosParaUsoSuelos(Set<Predio> predios) {
        this.prediosParaUsoSuelos = predios;
        return this;
    }

    public UsoSuelo addPrediosParaUsoSuelo(Predio predio) {
        this.prediosParaUsoSuelos.add(predio);
        predio.setUsoSuelo(this);
        return this;
    }

    public UsoSuelo removePrediosParaUsoSuelo(Predio predio) {
        this.prediosParaUsoSuelos.remove(predio);
        predio.setUsoSuelo(null);
        return this;
    }

    public void setPrediosParaUsoSuelos(Set<Predio> predios) {
        this.prediosParaUsoSuelos = predios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UsoSuelo usoSuelo = (UsoSuelo) o;
        if (usoSuelo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), usoSuelo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UsoSuelo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
