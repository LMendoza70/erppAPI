package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.SortNatural;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Set;
import java.util.Objects;


@Entity
@Table(name = "mueble_ante")
public class MuebleAnte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "muebleAnteGenerator")
    @SequenceGenerator(allocationSize = 1, name = "muebleAnteGenerator", sequenceName="mueble_ante_seq")
    private Long id;

    @ManyToOne
    private Antecedente antecedente;

    @ManyToOne
    private Mueble mueble;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Antecedente getAntecedente() {
        return antecedente;
    }

    public void setAntecedente(Antecedente antecedente) {
        this.antecedente = antecedente;
    }

    public Mueble getMueble() {
        return mueble;
    }

    public void setMueble(Mueble mueble) {
        this.mueble = mueble;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MuebleAnte{" +
            "id=" + getId() +
            "}";
    }
}