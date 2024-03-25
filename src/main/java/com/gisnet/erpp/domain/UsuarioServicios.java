package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UsuarioServicios.
 */
@Entity
@Table(name = "usuario_servicios")
public class UsuarioServicios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarioServiciosGenerator")
    @SequenceGenerator(allocationSize = 1, name = "usuarioServiciosGenerator", sequenceName="usuario_servicios_seq")
    private Long id;

    @Column(name = "ind_registrar")
    private Boolean indRegistrar;

    @Column(name = "ind_turnado")
    private Boolean indTurnado;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Servicio servicio;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIndRegistrar(){
        return indRegistrar;
    }

    public UsuarioServicios indRegistrar(Boolean indRegistrar){
        this.indRegistrar = indRegistrar;
        return this;
    }

    public void setIndRegistrar(Boolean indRegistrar){
        this.indRegistrar = indRegistrar;
    }

    public Boolean getIndTurnado(){
        return indTurnado;
    }

    public UsuarioServicios indTurnado(Boolean indTurnado){
        this.indTurnado = indTurnado;
        return this;
    }

    public void setIndTurnado(Boolean indTurnado){
        this.indTurnado = indTurnado;
    }


    public Usuario getUsuario(){
        return usuario;
    }

    public UsuarioServicios usuario(Usuario usuario){
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    public Servicio getServicio(){
        return servicio;
    }

    public UsuarioServicios servicio(Servicio servicio){
        this.servicio = servicio;
        return this;
    }

    public void setServicio(Servicio servicio){
        this.servicio = servicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UsuarioServicios usuarioServicios = (UsuarioServicios) o;
        if (usuarioServicios.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), usuarioServicios.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UsuarioServicios{" +
            "id=" + getId() +
            "}";
    }
}