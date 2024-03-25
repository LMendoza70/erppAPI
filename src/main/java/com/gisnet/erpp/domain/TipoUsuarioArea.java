package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TipoUsuarioArea.
 */
@Entity
@Table(name = "tipo_usuario_area")
public class TipoUsuarioArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoUsuarioAreaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoUsuarioAreaGenerator", sequenceName="tipo_usuario_area_seq")
    private Long id;

    @ManyToOne
    private Area area;
    
    @ManyToOne
    private TipoUsuario tipoUsuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Area getArea() {
        return area;
    }

    public TipoUsuarioArea area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }
    
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public TipoUsuarioArea tipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        return this;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoUsuarioArea tipoUsuarioArea = (TipoUsuarioArea) o;
        if (tipoUsuarioArea.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoUsuarioArea.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoUsuarioArea{" +
            "id=" + getId() +
            "}";
    }
}
