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
@Table(name = "servicio_concepto_pago")
public class ServicioConceptoPago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "servicioConceptoPagoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "servicioConceptoPagoGenerator", sequenceName="servicio_concepto_pago_seq")
    private Long id;

    @ManyToOne
    private ServiciosCotizador serviciosCotizador;

    @ManyToOne
    private ConceptoPago conceptoPago;
    
    @ManyToOne
    private Servicio servicio;


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

    public ConceptoPago getConceptoPago() {
        return conceptoPago;
    }

    public void setConceptoPago(ConceptoPago conceptoPago) {
        this.conceptoPago = conceptoPago;
    }
    
    public Servicio getServicio(){
        return servicio;
    }
    
    public void setServicio(Servicio servicio){
        this.servicio = servicio;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServicioConceptoPago{" +
            "id=" + getId() +
            "}";
    }
}
