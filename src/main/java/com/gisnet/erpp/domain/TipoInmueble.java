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
 * A TipoInmueble.
 */
@Entity
@Table(name = "tipo_inmueble")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoInmueble implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoInmuebleGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoInmuebleGenerator", sequenceName="tipo_inmueble_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;
    
    @OneToMany(mappedBy = "tipoInmueble")
    @JsonIgnore
    private Set<Predio> prediosParaTiposInmueble = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Set<Predio> getPrediosParaTiposInmueble() {
        return prediosParaTiposInmueble;
    }

    public TipoInmueble prediosParaTiposInmueble(Set<Predio> predios) {
        this.prediosParaTiposInmueble = predios;
        return this;
    }

    public TipoInmueble addPrediosParaTiposInmueble(Predio predio) {
        this.prediosParaTiposInmueble.add(predio);
        predio.setTipoInmueble(this);
        return this;
    }

    public TipoInmueble removePrediosParaTiposInmueble(Predio predio) {
        this.prediosParaTiposInmueble.remove(predio);
        predio.setTipoInmueble(null);
        return this;
    }

    public void setPrediosParaTiposInmueble(Set<Predio> predios) {
        this.prediosParaTiposInmueble = predios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoInmueble tipoInmueble = (TipoInmueble) o;
        if (tipoInmueble.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoInmueble.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoInmueble{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
