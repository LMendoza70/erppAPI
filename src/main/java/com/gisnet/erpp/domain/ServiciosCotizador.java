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
@Table(name = "servicios_cotizador")
public class ServiciosCotizador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "serviciosCotizadorGenerator")
    @SequenceGenerator(allocationSize = 1, name = "serviciosCotizadorGenerator", sequenceName="servicios_cotizador_seq")
    private Long id;

    @Column(name = "nombre", length = 200)
    private String nombre;

    @Column(name = "nombre_largo", length = 200)
    private String nombreLargo;

    @Column(name = "leyenda_tipo_servicio", length = 200)
    private String leyendaTipoServicio;


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

    public String getNombreLargo() {
        return nombreLargo;
    }

    public void setNombreLargo(String nombreLargo) {
        this.nombreLargo = nombreLargo;
    }

    public String getLeyendaTipoServicio() {
        return leyendaTipoServicio;
    }

    public void setLeyendaTipoServicio(String leyendaTipoServicio) {
        this.leyendaTipoServicio = leyendaTipoServicio;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiciosCotizador{" +
            "id=" + getId() +
            "}";
    }
}