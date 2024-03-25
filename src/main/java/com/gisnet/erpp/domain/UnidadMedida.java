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
 * A UnidadMedida.
 */
@Entity
@Table(name = "unidad_medida")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UnidadMedida implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unidadMedidaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "unidadMedidaGenerator", sequenceName="unidad_medida_seq")
    private Long id;

    @Size(max = 200)
    @Column(name = "nombre", length = 200)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "unidadMedida")
    @JsonIgnore
    private Set<Predio> prediosParaUnidadMedidas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public UnidadMedida nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public UnidadMedida activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Predio> getPrediosParaUnidadMedidas() {
        return prediosParaUnidadMedidas;
    }

    public UnidadMedida prediosParaUnidadMedidas(Set<Predio> predios) {
        this.prediosParaUnidadMedidas = predios;
        return this;
    }

    public UnidadMedida addPrediosParaUnidadMedida(Predio predio) {
        this.prediosParaUnidadMedidas.add(predio);
        predio.setUnidadMedida(this);
        return this;
    }

    public UnidadMedida removePrediosParaUnidadMedida(Predio predio) {
        this.prediosParaUnidadMedidas.remove(predio);
        predio.setUnidadMedida(null);
        return this;
    }

    public void setPrediosParaUnidadMedidas(Set<Predio> predios) {
        this.prediosParaUnidadMedidas = predios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnidadMedida unidadMedida = (UnidadMedida) o;
        if (unidadMedida.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), unidadMedida.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UnidadMedida{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
