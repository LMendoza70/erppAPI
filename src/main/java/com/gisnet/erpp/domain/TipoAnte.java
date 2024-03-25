package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.Objects;

/**
 * A TipoAnte.
 */
@Entity
@Table(name = "tipo_ante")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoAnte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoAnteGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoAnteGenerator", sequenceName="tipo_ante_seq")
    private Long id;

    @Column(name = "nombre")
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
        TipoAnte tipoAnte = (TipoAnte) o;
        if (tipoAnte.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoAnte.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoAnte{" +
            "id=" + getId() +
            "}";
    }
}
