package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TipoUsuarioClasifActo.
 */
@Entity
@Table(name = "tipo_usuario_clasif_acto")
public class TipoUsuarioClasifActo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoUsuarioClasifActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoUsuarioClasifActoGenerator", sequenceName="tipo_usuario_clasif_acto_seq")
    private Long id;

    @ManyToOne
    private ClasifActo clasifActo;
    
    @ManyToOne
    private TipoUsuario tipoUsuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClasifActo getClasifActo() {
        return clasifActo;
    }

    public TipoUsuarioClasifActo clasifActo(ClasifActo clasifActo) {
        this.clasifActo = clasifActo;
        return this;
    }

    public void setClasifActo(ClasifActo clasifActo) {
        this.clasifActo = clasifActo;
    }
    
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public TipoUsuarioClasifActo tipoUsuario(TipoUsuario tipoUsuario) {
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
        TipoUsuarioClasifActo tipoUsuarioClasifActo = (TipoUsuarioClasifActo) o;
        if (tipoUsuarioClasifActo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoUsuarioClasifActo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoUsuarioClasifActo{" +
            "id=" + getId() +
            "}";
    }
}
