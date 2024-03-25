package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import java.sql.Timestamp;

/**
 * A TipoPeriodo.
 */
@Entity
@Table(name = "tipo_periodo")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoPeriodo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoPeriodoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoPeriodoGenerator", sequenceName="tipo_periodo_seq")
    private Long id;

    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
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
        TipoPeriodo tipoPeriodo = (TipoPeriodo) o;
        if (tipoPeriodo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoPeriodo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoPeriodo{" +
            "id=" + getId() +
            "}";
    }
}
