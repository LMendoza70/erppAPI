package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UsuarioActos.
 */
@Entity
@Table(name = "usuario_actos")
public class UsuarioActos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarioActosGenerator")
    @SequenceGenerator(allocationSize = 1, name = "usuarioActosGenerator", sequenceName="usuario_actos_seq")
    private Long id;

    @Column(name = "ind_registrar")
    private Boolean indRegistrar;

    @Column(name = "ind_turnado")
    private Boolean indTurnado;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private TipoActo tipoActo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIndRegistrar(){
        return indRegistrar;
    }

    public UsuarioActos indRegistrar(Boolean indRegistrar){
        this.indRegistrar = indRegistrar;
        return this;
    }

    public void setIndRegistrar(Boolean indRegistrar){
        this.indRegistrar = indRegistrar;
    }

    public Boolean getIndTurnado(){
        return indTurnado;
    }

    public UsuarioActos indTurnado(Boolean indTurnado){
        this.indTurnado = indTurnado;
        return this;
    }

    public void setIndTurnado(Boolean indTurnado){
        this.indTurnado = indTurnado;
    }


    public Usuario getUsuario(){
        return usuario;
    }

    public UsuarioActos usuario(Usuario usuario){
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    public TipoActo getTipoActo(){
        return tipoActo;
    }

    public UsuarioActos tipoActo(TipoActo tipoActo){
        this.tipoActo = tipoActo;
        return this;
    }

    public void setTipoActo(TipoActo tipoActo){
        this.tipoActo = tipoActo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UsuarioActos usuarioActos = (UsuarioActos) o;
        if (usuarioActos.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), usuarioActos.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UsuarioActos{" +
            "id=" + getId() +
            "}";
    }
}