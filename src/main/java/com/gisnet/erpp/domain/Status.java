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
 * A Status.
 */
@Entity
@Table(name = "status")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Status implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statusGenerator")
    @SequenceGenerator(allocationSize = 1, name = "statusGenerator", sequenceName="status_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;
    
    @ManyToOne
    private StatusExterno statusExterno;

    @OneToMany(mappedBy = "status")
    @JsonIgnore
    private Set<Bitacora> bitacorasParaStatuses = new HashSet<>();

    @OneToMany(mappedBy = "status")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaStatuses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Status nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Bitacora> getBitacorasParaStatuses() {
        return bitacorasParaStatuses;
    }

    public Status bitacorasParaStatuses(Set<Bitacora> bitacoras) {
        this.bitacorasParaStatuses = bitacoras;
        return this;
    }

    public Status addBitacorasParaStatus(Bitacora bitacora) {
        this.bitacorasParaStatuses.add(bitacora);
        bitacora.setStatus(this);
        return this;
    }

    public Status removeBitacorasParaStatus(Bitacora bitacora) {
        this.bitacorasParaStatuses.remove(bitacora);
        bitacora.setStatus(null);
        return this;
    }

    public void setBitacorasParaStatuses(Set<Bitacora> bitacoras) {
        this.bitacorasParaStatuses = bitacoras;
    }

    public Set<Prelacion> getPrelacionesParaStatuses() {
        return prelacionesParaStatuses;
    }

    public Status prelacionesParaStatuses(Set<Prelacion> prelacions) {
        this.prelacionesParaStatuses = prelacions;
        return this;
    }

    public Status addPrelacionesParaStatus(Prelacion prelacion) {
        this.prelacionesParaStatuses.add(prelacion);
        prelacion.setStatus(this);
        return this;
    }

    public Status removePrelacionesParaStatus(Prelacion prelacion) {
        this.prelacionesParaStatuses.remove(prelacion);
        prelacion.setStatus(null);
        return this;
    }

    public void setPrelacionesParaStatuses(Set<Prelacion> prelacions) {
        this.prelacionesParaStatuses = prelacions;
    }
    
    public StatusExterno getStatusExterno() {
		return statusExterno;
	}

	public void setStatusExterno(StatusExterno statusExterno) {
		this.statusExterno = statusExterno;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Status status = (Status) o;
        if (status.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), status.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Status{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
