package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UsuarioOfiAreas.
 */
@Entity
@Table(name = "usuario_ofi_areas")
public class UsuarioOfiAreas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarioOfiAreasGenerator")
    @SequenceGenerator(allocationSize = 1, name = "usuarioOfiAreasGenerator", sequenceName="usuario_ofi_areas_seq")
    private Long id;

    @Column(name = "activo")
    private Boolean activo;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Area area;

    @ManyToOne
    private Oficina oficina;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActivo(){
        return activo;
    }

    public UsuarioOfiAreas activo(Boolean activo){
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo){
        this.activo = activo;
    }


    public Usuario getUsuario(){
        return usuario;
    }

    public UsuarioOfiAreas usuario(Usuario usuario){
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    public Area getArea(){
        return area;
    }

    public UsuarioOfiAreas area(Area area){
        this.area = area;
        return this;
    }

    public void setArea(Area area){
        this.area = area;
    }

    public Oficina getOficina(){
        return oficina;
    }

    public UsuarioOfiAreas oficina(Oficina oficina){
        this.oficina = oficina;
        return this;
    }

    public void setOficina(Oficina oficina){
        this.oficina = oficina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UsuarioOfiAreas usuarioOfiAreas = (UsuarioOfiAreas) o;
        if (usuarioOfiAreas.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), usuarioOfiAreas.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UsuarioOfiAreas{" +
            "id=" + getId() +
            "}";
    }
}