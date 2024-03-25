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
 * A Regimen.
 */
@Entity
@Table(name = "regimen")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Regimen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regimenGenerator")
    @SequenceGenerator(allocationSize = 1, name = "regimenGenerator", sequenceName="regimen_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "regimen")
    @JsonIgnore
    private Set<PredioPersona> predioPersonasParaRegimen = new HashSet<>();

    @ManyToOne
    private CampoValores campoValores;

    public CampoValores getCampoValores() {
		return campoValores;
	}

	public void setCampoValores(CampoValores campoValores) {
		this.campoValores = campoValores;
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

    public Regimen nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<PredioPersona> getPredioPersonasParaRegimen() {
        return predioPersonasParaRegimen;
    }

    public Regimen predioPersonasParaRegimen(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaRegimen = predioPersonas;
        return this;
    }

    public Regimen addPredioPersonasParaRegimen(PredioPersona predioPersona) {
        this.predioPersonasParaRegimen.add(predioPersona);
        predioPersona.setRegimen(this);
        return this;
    }

    public Regimen removePredioPersonasParaRegimen(PredioPersona predioPersona) {
        this.predioPersonasParaRegimen.remove(predioPersona);
        predioPersona.setRegimen(null);
        return this;
    }

    public void setPredioPersonasParaRegimen(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaRegimen = predioPersonas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Regimen regimen = (Regimen) o;
        if (regimen.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), regimen.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Regimen{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
