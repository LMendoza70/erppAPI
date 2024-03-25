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
 * A TipoIden.
 */
@Entity
@Table(name = "tipo_iden")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoIden implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoIdenGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoIdenGenerator", sequenceName="tipo_iden_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "tipoIden")
    @JsonIgnore
    private Set<Identificacion> identificacionesParaTipoIdens = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoIden nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public TipoIden activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Identificacion> getIdentificacionesParaTipoIdens() {
        return identificacionesParaTipoIdens;
    }

    public TipoIden identificacionesParaTipoIdens(Set<Identificacion> identificacions) {
        this.identificacionesParaTipoIdens = identificacions;
        return this;
    }

    public TipoIden addIdentificacionesParaTipoIden(Identificacion identificacion) {
        this.identificacionesParaTipoIdens.add(identificacion);
        identificacion.setTipoIden(this);
        return this;
    }

    public TipoIden removeIdentificacionesParaTipoIden(Identificacion identificacion) {
        this.identificacionesParaTipoIdens.remove(identificacion);
        identificacion.setTipoIden(null);
        return this;
    }

    public void setIdentificacionesParaTipoIdens(Set<Identificacion> identificacions) {
        this.identificacionesParaTipoIdens = identificacions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoIden tipoIden = (TipoIden) o;
        if (tipoIden.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoIden.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoIden{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
