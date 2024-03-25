package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TipoUsuarioTipoActo.
 */
@Entity
@Table(name = "tipo_usuario_tipo_acto")
public class TipoUsuarioTipoActo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoUsuarioTipoActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoUsuarioTipoActoGenerator", sequenceName="tipo_usuario_tipo_acto_seq")
    private Long id;

    @Column(name = "acceso_directo", length = 100)
    private String accesoDirecto;

    @ManyToOne
    private TipoActo tipoActo;
    
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

    public TipoUsuarioTipoActo accesoDirecto(String accesoDirecto) {
        this.accesoDirecto = accesoDirecto;
        return this;
    }

    public void setAccesoDirecto(String accesoDirecto) {
        this.accesoDirecto = accesoDirecto;
    }

    public TipoActo getTipoActo() {
        return tipoActo;
    }

    public TipoUsuarioTipoActo tipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
        return this;
    }

    public void setTipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
    }
    
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public TipoUsuarioTipoActo tipoUsuario(TipoUsuario tipoUsuario) {
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
        TipoUsuarioTipoActo tipoUsuarioTipoActo = (TipoUsuarioTipoActo) o;
        if (tipoUsuarioTipoActo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoUsuarioTipoActo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoUsuarioTipoActo{" +
            "id=" + getId() +
            "}";
    }
}
