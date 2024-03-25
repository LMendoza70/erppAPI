package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UsuarioClasifActoServicio.
 */
@Entity
@Table(name = "usuario_clasif_Acto_servicio")
public class UsuarioClasifActoServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarioClasifActoServicioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "usuarioClasifActoServicioGenerator", sequenceName="usu_clasif_acto_serv_seq")
    private Long id;

    @Column(name = "registrar_todos")
    private Integer registrarTodos;

    @Column(name = "turnar_todos")
    private Integer turnarTodos;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Servicio servicio;

    @ManyToOne
    private ClasifActo clasifActo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRegistrarTodos(){
        return registrarTodos;
    }

    public UsuarioClasifActoServicio registrarTodos(Integer registrarTodos){
        this.registrarTodos = registrarTodos;
        return this;
    }

    public void setRegistrarTodos(Integer registrarTodos){
        this.registrarTodos = registrarTodos;
    }

    public Integer getTurnarTodos(){
        return turnarTodos;
    }

    public UsuarioClasifActoServicio turnarTodos(Integer turnarTodos){
        this.turnarTodos = turnarTodos;
        return this;
    }

    public void setTurnarTodos(Integer turnarTodos){
        this.turnarTodos = turnarTodos;
    }


    public Usuario getUsuario(){
        return usuario;
    }

    public UsuarioClasifActoServicio usuario(Usuario usuario){
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    public Servicio getServicio(){
        return servicio;
    }

    public UsuarioClasifActoServicio servicio(Servicio servicio){
        this.servicio = servicio;
        return this;
    }

    public void setServicio(Servicio servicio){
        this.servicio = servicio;
    }

    public ClasifActo getClasifActo(){
        return clasifActo;
    }

    public UsuarioClasifActoServicio clasifActo(ClasifActo clasifActo){
        this.clasifActo = clasifActo;
        return this;
    }

    public void setClasifActo(ClasifActo clasifActo){
        this.clasifActo = clasifActo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UsuarioClasifActoServicio usuarioClasifActoServicio = (UsuarioClasifActoServicio) o;
        if (usuarioClasifActoServicio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), usuarioClasifActoServicio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UsuarioClasifActoServicio{" +
            "id=" + getId() +
            "}";
    }
}