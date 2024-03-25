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
 * A StatusActo.
 */
@Entity
@Table(name = "status_acto")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class StatusActo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statusActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "statusActoGenerator", sequenceName="status_acto_seq")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "statusActo")
    @JsonIgnore
    private Set<Acto> actosParaStatusActos = new HashSet<>();
    
    @OneToMany(mappedBy = "statusActo")
    @JsonIgnore
    private Set<Predio> prediosParaStatusActos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public StatusActo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Acto> getActosParaStatusActos() {
        return actosParaStatusActos;
    }

    public StatusActo actosParaStatusActos(Set<Acto> actos) {
        this.actosParaStatusActos = actos;
        return this;
    }

    public StatusActo addActosParaStatusActo(Acto acto) {
        this.actosParaStatusActos.add(acto);
        acto.setStatusActo(this);
        return this;
    }

    public StatusActo removeActosParaStatusActo(Acto acto) {
        this.actosParaStatusActos.remove(acto);
        acto.setStatusActo(null);
        return this;
    }

    public void setActosParaStatusActos(Set<Acto> actos) {
        this.actosParaStatusActos = actos;
    }
    
    public Set<Predio> getPrediosParaStatusActos() {
		return prediosParaStatusActos;
	}

	public void setPrediosParaStatusActos(Set<Predio> prediosParaStatusActos) {
		this.prediosParaStatusActos = prediosParaStatusActos;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatusActo statusActo = (StatusActo) o;
        if (statusActo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), statusActo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StatusActo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
