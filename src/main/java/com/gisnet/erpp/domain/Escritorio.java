package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.Date;

/**
 * A Escritorio.
 */
@Entity
@Table(name = "escritorio")
public class Escritorio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "escritorioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "escritorioGenerator", sequenceName="escritorio_seq")
    private Long id;

    @Column(name = "dia_escritorio")
    private Date diaEscritorio;

    @Column(name="ponderacion")
    private Integer ponderacion;

    @ManyToOne
    private Usuario usuario;
    
    @ManyToOne
    private Rol rol;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDiaEscritorio(){
        return diaEscritorio;
    }

    public Escritorio diaEscritorio(Date diaEscritorio){
        this.diaEscritorio = diaEscritorio;
        return this;
    }

    public void setDiaEscritorio(Date diaEscritorio){
        this.diaEscritorio = diaEscritorio;
    }

    public Integer getPonderacion(){
        return ponderacion;
    }

    public Escritorio ponderacion(Integer ponderacion){
        this.ponderacion = ponderacion;
        return this;
    }

    public void setPonderacion(Integer ponderacion){
        this.ponderacion = ponderacion;
    }

    public Usuario getUsuario(){
        return usuario;
    }

    public Escritorio usuario(Usuario usuario){
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario){
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
        Escritorio escritorio = (Escritorio) o;
        if (escritorio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), escritorio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Escritorio{" +
            "id=" + getId() +
            "}";
    }

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
}