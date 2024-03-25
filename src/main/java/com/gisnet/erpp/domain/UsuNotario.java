package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UsuNotario.
 */
@Entity
@Table(name = "usu_notario")
public class UsuNotario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuNotarioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "usuNotarioGenerator", sequenceName="usu_notario_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Notario notario;

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Notario getNotario() {
        return notario;
    }

    public UsuNotario notario(Notario notario) {
        this.notario = notario;
        return this;
    }

    public void setNotario(Notario notario) {
        this.notario = notario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public UsuNotario usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UsuNotario usuNotario = (UsuNotario) o;
        if (usuNotario.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), usuNotario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UsuNotario{" +
            "id=" + getId() +
            "}";
    }
}
