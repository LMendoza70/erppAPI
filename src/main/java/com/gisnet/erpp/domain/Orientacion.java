package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Orientacion.
 */
@Entity
@Table(name = "orientacion")
public class Orientacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orientacionGenerator")
    @SequenceGenerator(allocationSize = 1, name = "orientacionGenerator", sequenceName="orientacion_seq")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "nombre", length = 80, nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "orientacion")
    @JsonIgnore
    private Set<Colindancia> colindanciasParaOrientacions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Orientacion nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Orientacion activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Colindancia> getColindanciasParaOrientacions() {
        return colindanciasParaOrientacions;
    }

    public Orientacion colindanciasParaOrientacions(Set<Colindancia> colindancias) {
        this.colindanciasParaOrientacions = colindancias;
        return this;
    }

    public Orientacion addColindanciasParaOrientacion(Colindancia colindancia) {
        this.colindanciasParaOrientacions.add(colindancia);
        colindancia.setOrientacion(this);
        return this;
    }

    public Orientacion removeColindanciasParaOrientacion(Colindancia colindancia) {
        this.colindanciasParaOrientacions.remove(colindancia);
        colindancia.setOrientacion(null);
        return this;
    }

    public void setColindanciasParaOrientacions(Set<Colindancia> colindancias) {
        this.colindanciasParaOrientacions = colindancias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Orientacion orientacion = (Orientacion) o;
        if (orientacion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orientacion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Orientacion{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
