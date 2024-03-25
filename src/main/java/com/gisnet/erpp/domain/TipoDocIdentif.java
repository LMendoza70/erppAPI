package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CacheConcurrencyStrategy;
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
@Table(name = "tipo_doc_identif")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoDocIdentif implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoDocIdentifGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoDocIdentifGenerator", sequenceName="tipo_doc_identif_seq")
    private Long id;

    @Column(name = "nombre", length = 100)
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
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoDocIdentif{" +
            "id=" + getId() +
            "}";
    }
}