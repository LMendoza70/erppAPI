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
 * A TipoMotivo.
 */
@Entity
@Table(name = "tipo_motivo")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoMotivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoMotivoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoMotivoGenerator", sequenceName="tipo_motivo_seq")
    private Long id;

    @Size(max = 200)
    @Column(name = "nombre", length = 200)
    private String nombre;

    @OneToMany(mappedBy = "tipoMotivo")
    @JsonIgnore
    private Set<Motivo> motivosParaTipoMotivos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoMotivo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Motivo> getMotivosParaTipoMotivos() {
        return motivosParaTipoMotivos;
    }

    public TipoMotivo motivosParaTipoMotivos(Set<Motivo> motivos) {
        this.motivosParaTipoMotivos = motivos;
        return this;
    }

    public TipoMotivo addMotivosParaTipoMotivo(Motivo motivo) {
        this.motivosParaTipoMotivos.add(motivo);
        motivo.setTipoMotivo(this);
        return this;
    }

    public TipoMotivo removeMotivosParaTipoMotivo(Motivo motivo) {
        this.motivosParaTipoMotivos.remove(motivo);
        motivo.setTipoMotivo(null);
        return this;
    }

    public void setMotivosParaTipoMotivos(Set<Motivo> motivos) {
        this.motivosParaTipoMotivos = motivos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoMotivo tipoMotivo = (TipoMotivo) o;
        if (tipoMotivo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoMotivo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoMotivo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
