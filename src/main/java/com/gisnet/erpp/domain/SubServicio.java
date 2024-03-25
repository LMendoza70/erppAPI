package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SubServicio.
 */
@Entity
@Table(name = "sub_servicio")
public class SubServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subServicioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "subServicioGenerator", sequenceName="sub_servicio_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "subServicio")
    @JsonIgnore
    private Set<SubSubServicio> subSubServiciosParaSubServicios = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Servicio servicio;
    
    @ManyToOne
    private ConceptoPago conceptoPago;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public SubServicio nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<SubSubServicio> getSubSubServiciosParaSubServicios() {
        return subSubServiciosParaSubServicios;
    }

    public SubServicio subSubServiciosParaSubServicios(Set<SubSubServicio> subSubServicios) {
        this.subSubServiciosParaSubServicios = subSubServicios;
        return this;
    }

    public SubServicio addSubSubServiciosParaSubServicio(SubSubServicio subSubServicio) {
        this.subSubServiciosParaSubServicios.add(subSubServicio);
        subSubServicio.setSubServicio(this);
        return this;
    }

    public SubServicio removeSubSubServiciosParaSubServicio(SubSubServicio subSubServicio) {
        this.subSubServiciosParaSubServicios.remove(subSubServicio);
        subSubServicio.setSubServicio(null);
        return this;
    }

    public void setSubSubServiciosParaSubServicios(Set<SubSubServicio> subSubServicios) {
        this.subSubServiciosParaSubServicios = subSubServicios;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public SubServicio servicio(Servicio servicio) {
        this.servicio = servicio;
        return this;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }
    
    public ConceptoPago getConceptoPago() {
        return conceptoPago;
    }

    public SubServicio conceptoPago(ConceptoPago conceptoPago) {
        this.conceptoPago = conceptoPago;
        return this;
    }

    public void setConceptoPago(ConceptoPago conceptoPago) {
        this.conceptoPago = conceptoPago;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubServicio subServicio = (SubServicio) o;
        if (subServicio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subServicio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubServicio{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
