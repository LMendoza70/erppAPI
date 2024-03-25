package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SeccionesPorOficina.
 */
@Entity
@Table(name = "secciones_x_oficina")
public class SeccionPorOficina implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seccionesPorOficinaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "seccionesPorOficinaGenerator", sequenceName="secciones_x_oficina_seq")
    private Long id;

    private String observaciones;

    @ManyToOne
    private Oficina oficina;

    @ManyToOne
    private Seccion seccion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservaciones(){
        return observaciones;
    }

    public SeccionPorOficina observaciones(String observaciones){
        this.observaciones = observaciones;
        return this;
    }

    public void setObservaciones(String observaciones){
        this.observaciones = observaciones;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public SeccionPorOficina oficina(Oficina oficina) {
        this.oficina = oficina;
        return this;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public SeccionPorOficina seccion(Seccion seccion) {
        this.seccion = seccion;
        return this;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SeccionPorOficina seccionesPorOficina = (SeccionPorOficina) o;
        if (seccionesPorOficina.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), seccionesPorOficina.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SeccionesPorOficina{" +
            "id=" + getId() +
            "}";
    }
}