package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TipoUsuarioServicio.
 */
@Entity
@Table(name = "tipo_usuario_servicio")
public class TipoUsuarioServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoUsuarioServicioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoUsuarioServicioGenerator", sequenceName="tipo_usuario_servicio_seq")
    private Long id;

    @Column(name = "acceso_directo", length = 100)
    private String accesoDirecto;

    @ManyToOne
    private Servicio servicio;
    
    @ManyToOne
    private TipoUsuario tipoUsuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccesoDirecto() {
        return accesoDirecto;
    }

    public TipoUsuarioServicio accesoDirecto(String accesoDirecto) {
        this.accesoDirecto = accesoDirecto;
        return this;
    }

    public void setAccesoDirecto(String accesoDirecto) {
        this.accesoDirecto = accesoDirecto;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public TipoUsuarioServicio servicio(Servicio servicio) {
        this.servicio = servicio;
        return this;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }
    
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public TipoUsuarioServicio tipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        return this;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoUsuarioServicio tipoUsuarioServicio = (TipoUsuarioServicio) o;
        if (tipoUsuarioServicio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoUsuarioServicio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoUsuarioServicio{" +
            "id=" + getId() +
            "}";
    }
}
