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
@Table(name = "mueble_persona")
public class MueblePersona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mueblePersonaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "mueblePersonaGenerator", sequenceName="mueble_persona_seq")
    private Long id;

    @Column(name = "hash_fila", length = 80)
    private String hashFila;

    @Column(name = "menor_edad")
    private Boolean menorEdad;

    @Column(name = "porcentaje_dd")
    private Float porcentajeDd;

    @Column(name = "porcentaje_uv")
    private Float porcentajeUv;

    @ManyToOne
    private ActoPredio actoPredio;

    @ManyToOne
    private Direccion direccion;

    @ManyToOne
    private EstadoCivil estadoCivil;

    @ManyToOne
    private Nacionalidad nacionalidad;

    @ManyToOne
    private Parentesco parentesco;

    @ManyToOne
    private Persona persona;

    @ManyToOne
    private TipoRelPersona tipoRelPersona;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHashFila() {
        return hashFila;
    }

    public void setHashFila(String hashFila) {
        this.hashFila = hashFila;
    }

    public Boolean getMenorEdad() {
        return menorEdad;
    }

    public void setMenorEdad(Boolean menorEdad) {
        this.menorEdad = menorEdad;
    }

    public Float getPorcentajeDd() {
        return porcentajeDd;
    }

    public void setPorcentajeDd(Float porcentajeDd) {
        this.porcentajeDd = porcentajeDd;
    } 

    public Float getPorcentajeUv() {
        return porcentajeUv;
    }

    public void setPorcentajeUv(Float porcentajeUv) {
        this.porcentajeUv = porcentajeUv;
    }

    public ActoPredio getActoPredio() {
        return actoPredio;
    }

    public void setActoPredio(ActoPredio actoPredio) {
        this.actoPredio = actoPredio;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    } 

    public Nacionalidad getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public Parentesco getParentesco() {
        return parentesco;
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    } 

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    } 

    public TipoRelPersona getTipoRelPersona() {
        return tipoRelPersona;
    }

    public void setTipoRelPersona(TipoRelPersona tipoRelPersona) {
        this.tipoRelPersona = tipoRelPersona;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MueblePersona{" +
            "id=" + getId() +
            "}";
    }
}