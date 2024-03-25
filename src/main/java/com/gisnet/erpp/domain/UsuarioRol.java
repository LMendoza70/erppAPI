package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UsuarioRol.
 */
@Entity
@Table(name = "usuario_rol")
public class UsuarioRol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarioRolGenerator")
    @SequenceGenerator(allocationSize = 1, name = "usuarioRolGenerator", sequenceName="usuario_rol_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Rol rol;

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

    public Rol getRol() {
        return rol;
    }

    public UsuarioRol rol(Rol rol) {
        this.rol = rol;
        return this;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public UsuarioRol usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Area getArea() {
        return area;
    }

    public UsuarioRol area(Area area) {
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
        UsuarioRol usuarioRol = (UsuarioRol) o;
        if (usuarioRol.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), usuarioRol.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UsuarioRol{" +
            "id=" + getId() +
            "}";
    }
}
