package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A EstructuraOrg.
 */
@Entity
@Table(name = "estructura_org")
public class EstructuraOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estructuraOrgGenerator")
    @SequenceGenerator(allocationSize = 1, name = "estructuraOrgGenerator", sequenceName="estructura_org_seq")
    private Long id;

    private Boolean activo;

    @ManyToOne
    private Organizacion organizacion;

    @ManyToOne
    private Usuario usuarioMaster;

    @ManyToOne
    private Usuario usuarioDetail;
  

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public Usuario getUsuarioMaster() {
        return usuarioMaster;
    }

    public void setUsuarioMaster(Usuario usuarioMaster) {
        this.usuarioMaster = usuarioMaster;
    }

    public Usuario getUsuarioDetail() {
        return usuarioDetail;
    }

    public void setUsuarioDetail(Usuario usuarioDetail) {
        this.usuarioDetail = usuarioDetail;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EstructuraOrg estructuraOrg = (EstructuraOrg) o;
        if (estructuraOrg.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), estructuraOrg.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EstructuraOrg{" +
            "id=" + getId() +
            "}";
    }
}