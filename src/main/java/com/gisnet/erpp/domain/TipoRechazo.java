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
 * A TipoRechazo.
 */
@Entity
@Table(name = "tipo_rechazo")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoRechazo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoRechazoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoRechazoGenerator", sequenceName="tipo_rechazo_seq")
    private Long id;

    @Size(max = 500)
    @Column(name = "nombre", length = 500)
    private String nombre;

    @ManyToOne(optional = true)
    private TipoMotivo tipoMotivo;
    
 
    public TipoMotivo getTipoMotivo() {
		return tipoMotivo;
	}

	public void setTipoMotivo(TipoMotivo tipoMotivo) {
		this.tipoMotivo = tipoMotivo;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoRechazo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoRechazo tipoRechazo = (TipoRechazo) o;
        if (tipoRechazo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoRechazo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoRechazo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
