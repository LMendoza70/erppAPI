package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SubSubServicio.
 */
@Entity
@Table(name = "sub_sub_servicio")
public class SubSubServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subSubServicioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "subSubServicioGenerator", sequenceName="sub_sub_servicio_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @ManyToOne(optional = false)
    @NotNull
    private SubServicio subServicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public SubSubServicio nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public SubServicio getSubServicio() {
        return subServicio;
    }

    public SubSubServicio subServicio(SubServicio subServicio) {
        this.subServicio = subServicio;
        return this;
    }

    public void setSubServicio(SubServicio subServicio) {
        this.subServicio = subServicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubSubServicio subSubServicio = (SubSubServicio) o;
        if (subSubServicio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subSubServicio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubSubServicio{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
