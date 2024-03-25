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
 * A TipoEntrada.
 */
@Entity
@Table(name = "tipo_entrada")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class TipoEntrada implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoEntradaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoEntradaGenerator", sequenceName="tipo_entrada_seq")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "tipoEntrada")
    @JsonIgnore
    private Set<ActoPredio> actoPrediosParaTipoEntradas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoEntrada nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<ActoPredio> getActoPrediosParaTipoEntradas() {
        return actoPrediosParaTipoEntradas;
    }

    public TipoEntrada actoPrediosParaTipoEntradas(Set<ActoPredio> actoPredios) {
        this.actoPrediosParaTipoEntradas = actoPredios;
        return this;
    }

    public TipoEntrada addActoPrediosParaTipoEntrada(ActoPredio actoPredio) {
        this.actoPrediosParaTipoEntradas.add(actoPredio);
        actoPredio.setTipoEntrada(this);
        return this;
    }

    public TipoEntrada removeActoPrediosParaTipoEntrada(ActoPredio actoPredio) {
        this.actoPrediosParaTipoEntradas.remove(actoPredio);
        actoPredio.setTipoEntrada(null);
        return this;
    }

    public void setActoPrediosParaTipoEntradas(Set<ActoPredio> actoPredios) {
        this.actoPrediosParaTipoEntradas = actoPredios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoEntrada tipoEntrada = (TipoEntrada) o;
        if (tipoEntrada.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoEntrada.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoEntrada{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
