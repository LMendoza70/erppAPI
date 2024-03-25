package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A CampoValores.
 */
@Entity
@Table(name = "campo_valores")
public class CampoValores implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campoValoresGenerator")
    @SequenceGenerator(allocationSize = 1, name = "campoValoresGenerator", sequenceName="campo_valores_seq")
    private Long id;

    @Size(max = 200)
    @Column(name = "nombre", length = 200)
    private String nombre;

    @Column(name = "activo")
    private Boolean activo;

    @ManyToOne(optional = false)
    @NotNull
    private Campo campo;
    
    @OneToMany(mappedBy = "campoValor")
    @JsonIgnore
    private Set<CampoCancelaActo> cancelaActos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public CampoValores nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public CampoValores activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Campo getCampo() {
        return campo;
    }

    public CampoValores campo(Campo campo) {
        this.campo = campo;
        return this;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }
    
    public Set<CampoCancelaActo> getCancelaActos() {
        return cancelaActos;
    }

    @JsonProperty
    public CampoValores cancelaActo(Set<CampoCancelaActo> cancelaActos) {
        this.cancelaActos = cancelaActos;
        return this;
    }

    public CampoValores addCancelaActos(CampoCancelaActo cancelaActo) {
        this.cancelaActos.add(cancelaActo);
        cancelaActo.setCampoValor(this);
        return this;
    }

    public CampoValores removeCancelaActos(CampoCancelaActo cancelaActo) {
        this.cancelaActos.remove(cancelaActo);
        cancelaActo.setCampoValor(null);
        return this;
    }

    public void setCancelaActos(Set<CampoCancelaActo> cancelaActos) {
        this.cancelaActos = cancelaActos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CampoValores campoValores = (CampoValores) o;
        if (campoValores.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), campoValores.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CampoValores{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            ", cancelaActos='"+getCancelaActos()+ "'" +
            ", campo='" + (getCampo() != null ? getCampo().getId() : null) + "'" +
            "}";
    }
}
