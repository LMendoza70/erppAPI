package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TipoCampo.
 */
@Entity
@Table(name = "tipo_campo")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class TipoCampo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoCampoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoCampoGenerator", sequenceName="tipo_campo_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "tipoCampo")
    @JsonIgnore
    private Set<Campo> camposParaTipoCampos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoCampo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Campo> getCamposParaTipoCampos() {
        return camposParaTipoCampos;
    }

    public TipoCampo camposParaTipoCampos(Set<Campo> campos) {
        this.camposParaTipoCampos = campos;
        return this;
    }

    public TipoCampo addCamposParaTipoCampo(Campo campo) {
        this.camposParaTipoCampos.add(campo);
        campo.setTipoCampo(this);
        return this;
    }

    public TipoCampo removeCamposParaTipoCampo(Campo campo) {
        this.camposParaTipoCampos.remove(campo);
        campo.setTipoCampo(null);
        return this;
    }

    public void setCamposParaTipoCampos(Set<Campo> campos) {
        this.camposParaTipoCampos = campos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoCampo tipoCampo = (TipoCampo) o;
        if (tipoCampo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoCampo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoCampo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
