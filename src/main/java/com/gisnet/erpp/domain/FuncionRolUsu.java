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
@Table(name = "funcion_rol_usu")
public class FuncionRolUsu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "funcionRolUsuGenerator")
    @SequenceGenerator(allocationSize = 1, name = "funcionRolUsuGenerator", sequenceName="funcion_rol_usu_seq")
    private Long id;
    
    private Boolean activo;

    @ManyToOne
    private FuncionRol funcionRol;

    @ManyToOne
    private Usuario usuario;
    

    public Long getId() {
        return id;
    }
    
    public Boolean isActivo() {
        return this.activo;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setIsActivo(Boolean isActivo) {
        this.activo = isActivo;
    }
    
    public FuncionRolUsu Activo(Boolean isActivo) {
        this.activo = isActivo;
        return this;
    }

    public FuncionRol getFuncionRol() {
        return funcionRol;
    }

    public void setFuncionRol(FuncionRol funcionRol) {
        this.funcionRol = funcionRol;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FuncionRolUsu{" +
            "id=" + getId() +
            "isActivo=" + this.isActivo().toString()  +
            "}";
    }
}