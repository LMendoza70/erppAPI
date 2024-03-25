package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * A MesaTema.
 */
@Entity
@Table(name = "mesa_tema")
public class MesaTema implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mesaTemaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "mesaTemaGenerator", sequenceName="mesa_tema_seq")
    private Long id;

    @Size(max = 200)
    @Column(name = "nombre", length = 200)
    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public MesaTema nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

   

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MesaTema mesaTema = (MesaTema) o;
        if (mesaTema.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mesaTema.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MesaTema{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
