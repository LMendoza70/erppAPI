package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Objects;

/**
 * A AreaClasifActo.
 */
@Entity
@Table(name = "area_rol")
public class AreaRol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "areaRolGenerator")
    @SequenceGenerator(allocationSize = 1, name = "areaRolGenerator", sequenceName="area_rol_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Area area;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnore
    private Rol rol;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Area getArea() {
        return area;
    }

    public AreaRol area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Rol getRol() {
        return this.rol;
    }

    public AreaRol rol(Rol rol) {
        this.rol = rol;
        return this;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AreaRol areaRol = (AreaRol) o;
        if (areaRol.getId() == null || this.getId() == null) {
            return false;
        }
        return Objects.equals(getId(), areaRol.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }

    @Override
    public String toString() {
        return "AreaRol{" +
            "id=" + getId() +
            "AreaId=" + this.area.getId() +
            "RolId=" + this.rol.getId() +
            "}";
    }
}
