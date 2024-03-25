package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import java.sql.Timestamp;

/**
 * A Periodo.
 */
@Entity
@Table(name = "periodo")
public class Periodo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "periodoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "periodoGenerator", sequenceName="periodo_seq")
    private Long id;

    private Timestamp fin;

    private Timestamp inicio;

    @ManyToOne
    private TipoPeriodo tipoPeriodo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getFin(){
        return fin;
    }

    public void setFin(Timestamp fin){
        this.fin = fin;
    }

    public Timestamp getInicio(){
        return inicio;
    }

    public void setInicio(Timestamp inicio){
        this.inicio = inicio;
    }

    public TipoPeriodo getTipoPeriodo(){
        return tipoPeriodo;
    }

    public Periodo tipoPeriodo(TipoPeriodo tipoPeriodo){
        this.tipoPeriodo = tipoPeriodo;
        return this;
    }

    public void setTipoPeriodo(TipoPeriodo tipoPeriodo){
        this.tipoPeriodo = tipoPeriodo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Periodo periodo = (Periodo) o;
        if (periodo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), periodo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Periodo{" +
            "id=" + getId() +
            "}";
    }
}
