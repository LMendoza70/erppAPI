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
@Table(name = "gestor_notario")
public class GestorNotario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gestorNotarioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "gestorNotarioGenerator", sequenceName="gestor_notario_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnore
    private Notario notario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public GestorNotario usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Notario getNotario() {
        return this.notario;
    }

    public GestorNotario notario(Notario notario) {
        this.notario = notario;
        return this;
    }

    public void setNotario(Notario notario) {
        this.notario = notario;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GestorNotario gestorNotario = (GestorNotario) o;
        if (gestorNotario.getId() == null || this.getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gestorNotario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }

    @Override
    public String toString() {
        return "AreaRol{" +
            "id=" + getId() +
            "usuarioId=" + this.usuario.getId() +
            "notarioId=" + this.notario.getId() +
            "}";
    }
}
