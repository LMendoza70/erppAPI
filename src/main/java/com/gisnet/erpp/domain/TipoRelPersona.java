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
 * A TipoRelPersona.
 */
@Entity
@Table(name = "tipo_rel_persona")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoRelPersona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoRelPersonaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoRelPersonaGenerator", sequenceName="tipo_rel_persona_seq")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "nombre", length = 30, nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "tipoRelPersona")
    @JsonIgnore
    private Set<PredioPersona> predioPersonasParaTipoRelPersonas = new HashSet<>();

    @ManyToOne
    private CampoValores campoValor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoRelPersona nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public TipoRelPersona activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<PredioPersona> getPredioPersonasParaTipoRelPersonas() {
        return predioPersonasParaTipoRelPersonas;
    }

    public TipoRelPersona predioPersonasParaTipoRelPersonas(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaTipoRelPersonas = predioPersonas;
        return this;
    }

    public TipoRelPersona addPredioPersonasParaTipoRelPersona(PredioPersona predioPersona) {
        this.predioPersonasParaTipoRelPersonas.add(predioPersona);
        predioPersona.setTipoRelPersona(this);
        return this;
    }

    public TipoRelPersona removePredioPersonasParaTipoRelPersona(PredioPersona predioPersona) {
        this.predioPersonasParaTipoRelPersonas.remove(predioPersona);
        predioPersona.setTipoRelPersona(null);
        return this;
    }

    public void setPredioPersonasParaTipoRelPersonas(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaTipoRelPersonas = predioPersonas;
    }

    public CampoValores getCampoValor() {
		return campoValor;
	}

	public void setCampoValor(CampoValores campoValor) {
		this.campoValor = campoValor;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoRelPersona tipoRelPersona = (TipoRelPersona) o;
        if (tipoRelPersona.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoRelPersona.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoRelPersona{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
