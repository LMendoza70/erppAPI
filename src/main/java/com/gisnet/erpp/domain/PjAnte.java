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
@Table(name = "pj_ante")
public class PjAnte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pjAnteGenerator")
    @SequenceGenerator(allocationSize = 1, name = "pjAnteGenerator", sequenceName="pj_ante_seq")
    private Long id;

    @ManyToOne
    private Antecedente antecedente;

    @ManyToOne
    private PersonaJuridica personaJuridica;


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

    public PersonaJuridica getPersonaJuridica() {
        return personaJuridica;
    }

    public void setPersonaJuridica(PersonaJuridica personaJuridica) {
        this.personaJuridica = personaJuridica;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PjAnte{" +
            "id=" + getId() +
            "}";
    }
}