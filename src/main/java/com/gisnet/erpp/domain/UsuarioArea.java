package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UsuarioArea.
 */
@Entity
@Table(name = "usuario_area")
public class UsuarioArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarioAreaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "usuarioAreaGenerator", sequenceName="usuario_area_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;

    @ManyToOne
    private Area area;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public UsuarioArea usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Area getArea() {
        return area;
    }

    public UsuarioArea area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UsuarioArea usuarioArea = (UsuarioArea) o;
        if (usuarioArea.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), usuarioArea.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UsuarioArea{" +
            "id=" + getId() +
            "}";
    }
}
