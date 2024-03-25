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
@Table(name = "auxiliar_ante")
public class AuxiliarAnte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auxiliarAnteGenerator")
    @SequenceGenerator(allocationSize = 1, name = "auxiliarAnteGenerator", sequenceName="auxiliar_ante_seq")
    private Long id;

    @ManyToOne
    private Antecedente antecedente;

    @ManyToOne
    private FolioSeccionAuxiliar folioSeccionAuxiliar;


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

    public FolioSeccionAuxiliar getFolioSeccionAuxiliar() {
        return folioSeccionAuxiliar;
    }

    public void setFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar) {
        this.folioSeccionAuxiliar = folioSeccionAuxiliar;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AuxiliarAnte{" +
            "id=" + getId() +
            "}";
    }
}