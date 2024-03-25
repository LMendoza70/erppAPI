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
@Table(name = "servicio_clasif_concepto")
public class ServicioClasifConcepto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "servicioClasifConceptoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "servicioClasifConceptoGenerator", sequenceName="servicio_clasif_concepto_seq")
    private Long id;

    @ManyToOne
    private ServiciosCotizador serviciosCotizador;

    @ManyToOne
    private ClasificacionConcepto clasificacionConcepto;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public ServiciosCotizador getServiciosCotizador() {
        return serviciosCotizador;
    }

    public void setServiciosCotizador(ServiciosCotizador serviciosCotizador) {
        this.serviciosCotizador = serviciosCotizador;
    }

    public ClasificacionConcepto getClasificacionConcepto() {
        return clasificacionConcepto;
    }

    public void setClasificacionConcepto(ClasificacionConcepto clasificacionConcepto) {
        this.clasificacionConcepto = clasificacionConcepto;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServicioClasifConcepto{" +
            "id=" + getId() +
            "}";
    }
}