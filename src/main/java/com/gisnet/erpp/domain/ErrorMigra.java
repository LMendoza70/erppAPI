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
 * A ErrorMigra.
 */
@Entity
@Table(name = "error_migra")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ErrorMigra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "errorMigraGenerator")
    @SequenceGenerator(allocationSize = 1, name = "errorMigraGenerator", sequenceName="error_migra_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "errorMigra")
    @JsonIgnore
    private Set<Predio> prediosParaErrorMigras = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public ErrorMigra nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Predio> getPrediosParaErrorMigras() {
        return prediosParaErrorMigras;
    }

    public ErrorMigra prediosParaErrorMigras(Set<Predio> predios) {
        this.prediosParaErrorMigras = predios;
        return this;
    }

    public ErrorMigra addPrediosParaErrorMigra(Predio predio) {
        this.prediosParaErrorMigras.add(predio);
        predio.setErrorMigra(this);
        return this;
    }

    public ErrorMigra removePrediosParaErrorMigra(Predio predio) {
        this.prediosParaErrorMigras.remove(predio);
        predio.setErrorMigra(null);
        return this;
    }

    public void setPrediosParaErrorMigras(Set<Predio> predios) {
        this.prediosParaErrorMigras = predios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorMigra errorMigra = (ErrorMigra) o;
        if (errorMigra.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), errorMigra.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ErrorMigra{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
