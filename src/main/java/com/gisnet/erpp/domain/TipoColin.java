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
 * A TipoColin.
 */
@Entity
@Table(name = "tipo_colin")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoColin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoColinGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoColinGenerator", sequenceName="tipo_colin_seq")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "nombre", length = 80, nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "tipoColin")
    @JsonIgnore
    private Set<Colindancia> colindanciasParaTipoColins = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoColin nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public TipoColin activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Colindancia> getColindanciasParaTipoColins() {
        return colindanciasParaTipoColins;
    }

    public TipoColin colindanciasParaTipoColins(Set<Colindancia> colindancias) {
        this.colindanciasParaTipoColins = colindancias;
        return this;
    }

    public TipoColin addColindanciasParaTipoColin(Colindancia colindancia) {
        this.colindanciasParaTipoColins.add(colindancia);
        colindancia.setTipoColin(this);
        return this;
    }

    public TipoColin removeColindanciasParaTipoColin(Colindancia colindancia) {
        this.colindanciasParaTipoColins.remove(colindancia);
        colindancia.setTipoColin(null);
        return this;
    }

    public void setColindanciasParaTipoColins(Set<Colindancia> colindancias) {
        this.colindanciasParaTipoColins = colindancias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoColin tipoColin = (TipoColin) o;
        if (tipoColin.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoColin.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoColin{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
