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
@Table(name = "usu_arearol")
public class UsuarioAreaRol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarioAreaRolGenerator")
    @SequenceGenerator(allocationSize = 1, name = "usuarioAreaRolGenerator", sequenceName="usu_arearol_seq")
    private Long id;
    
    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private AreaRol areaRol;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public AreaRol setAreaRol() {
        return areaRol;
    }

    public void setAreaRol(AreaRol areaRol) {
        this.areaRol = areaRol;
    }
    
    public AreaRol getAreaRol() {
        return this.areaRol;
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
            "}";
    }
}