package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FuncionRol.
 */
@Entity
@Table(name = "funcion_rol")
public class FuncionRol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "funcionRolGenerator")
    @SequenceGenerator(allocationSize = 1, name = "funcionRolGenerator", sequenceName="funcion_rol_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Funcion funcion;

    @ManyToOne(optional = false)
    @NotNull
    private Rol rol;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public FuncionRol funcion(Funcion funcion) {
        this.funcion = funcion;
        return this;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public Rol getRol() {
        return rol;
    }

    public FuncionRol rol(Rol rol) {
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
        FuncionRol funcionRol = (FuncionRol) o;
        if (funcionRol.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), funcionRol.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FuncionRol{" +
            "id=" + getId() +
            "}";
    }
}
