package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PredioPersona.
 */
@Entity
@Table(name = "predio_persona")
public class PredioPersona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "predioPersonaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "predioPersonaGenerator", sequenceName="predio_persona_seq")
    private Long id;

    @Column(name = "porcentaje_dd")
    private Float porcentajeDd;

    @Column(name = "porcentaje_uv")
    private Float porcentajeUv;
    
    @Column(name = "superficie")
    private Double superficie;

    @Size(max = 80)
    @Column(name = "hash_fila", length = 80)
    private String hashFila;

    @Column(name = "menor_edad")
    private Boolean menorEdad;

    @Column(name = "estatus")
    private Boolean estatus;

    @OneToMany(mappedBy = "predioPropDesignan")
    @JsonIgnore
    private Set<PredioPersona> predioPersonasParaPredioPropDesignans = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private TipoRelPersona tipoRelPersona;

    @ManyToOne
    private Direccion direccion;

    @ManyToOne
    private Parentesco parentesco;

    @ManyToOne
    private PredioPersona predioPropDesignan;

    @ManyToOne(optional = false)
    private ActoPredio actoPredio;

    @ManyToOne
    private EstadoCivil estadoCivil;

    @ManyToOne
    private Regimen regimen;

    @ManyToOne
    private Nacionalidad nacionalidad;

    @ManyToOne(optional = false)
    @NotNull
    private Persona persona;
    
    @Column(name = "primer_registro", length = 1)
    private Boolean primerRegistro = false;
    
    @Column(name = "designo_beneficiario")
    private Boolean designoBeneficiario;
    
    @Column(name = "extinto")
    private Boolean extinto;
    
    
    public Boolean getDesignoBeneficiario() {
		return designoBeneficiario==null?false:designoBeneficiario;
	}

	public void setDesignoBeneficiario(Boolean designoBeneficiario) {
		this.designoBeneficiario = designoBeneficiario;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPorcentajeDd() {
        return porcentajeDd;
    }

    public PredioPersona porcentajeDd(Float porcentajeDd) {
        this.porcentajeDd = porcentajeDd;
        return this;
    }

    public void setPorcentajeDd(Float porcentajeDd) {
        this.porcentajeDd = porcentajeDd;
    }

    public Float getPorcentajeUv() {
        return porcentajeUv;
    }

    public PredioPersona porcentajeUv(Float porcentajeUv) {
        this.porcentajeUv = porcentajeUv;
        return this;
    }

    public void setPorcentajeUv(Float porcentajeUv) {
        this.porcentajeUv = porcentajeUv;
    }

    public String getHashFila() {
        return hashFila;
    }

    public PredioPersona hashFila(String hashFila) {
        this.hashFila = hashFila;
        return this;
    }

    public void setHashFila(String hashFila) {
        this.hashFila = hashFila;
    }

    public Boolean isMenorEdad() {
        return menorEdad;
    }

    public PredioPersona menorEdad(Boolean menorEdad) {
        this.menorEdad = menorEdad;
        return this;
    }

    public void setMenorEdad(Boolean menorEdad) {
        this.menorEdad = menorEdad;
    }

    public Boolean getEstatus() {
        return estatus;
    }

    public PredioPersona estatus(Boolean estatus) {
        this.estatus = estatus;
        return this;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }

    public Set<PredioPersona> getPredioPersonasParaPredioPropDesignans() {
        return predioPersonasParaPredioPropDesignans;
    }

    public PredioPersona predioPersonasParaPredioPropDesignans(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaPredioPropDesignans = predioPersonas;
        return this;
    }

    public PredioPersona addPredioPersonasParaPredioPropDesignan(PredioPersona predioPersona) {
        this.predioPersonasParaPredioPropDesignans.add(predioPersona);
        predioPersona.setPredioPropDesignan(this);
        return this;
    }

    public PredioPersona removePredioPersonasParaPredioPropDesignan(PredioPersona predioPersona) {
        this.predioPersonasParaPredioPropDesignans.remove(predioPersona);
        predioPersona.setPredioPropDesignan(null);
        return this;
    }

    public void setPredioPersonasParaPredioPropDesignans(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaPredioPropDesignans = predioPersonas;
    }

    public TipoRelPersona getTipoRelPersona() {
        return tipoRelPersona;
    }

    public PredioPersona tipoRelPersona(TipoRelPersona tipoRelPersona) {
        this.tipoRelPersona = tipoRelPersona;
        return this;
    }

    public void setTipoRelPersona(TipoRelPersona tipoRelPersona) {
        this.tipoRelPersona = tipoRelPersona;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public PredioPersona direccion(Direccion direccion) {
        this.direccion = direccion;
        return this;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Parentesco getParentesco() {
        return parentesco;
    }

    public PredioPersona parentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
        return this;
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public PredioPersona getPredioPropDesignan() {
        return predioPropDesignan;
    }

    public PredioPersona predioPropDesignan(PredioPersona predioPersona) {
        this.predioPropDesignan = predioPersona;
        return this;
    }

    public void setPredioPropDesignan(PredioPersona predioPersona) {
        this.predioPropDesignan = predioPersona;
    }

    public ActoPredio getActoPredio() {
        return actoPredio;
    }

    public PredioPersona actoPredio(ActoPredio actoPredio) {
        this.actoPredio = actoPredio;
        return this;
    }

    public void setActoPredio(ActoPredio actoPredio) {
        this.actoPredio = actoPredio;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public PredioPersona estadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
        return this;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Regimen getRegimen() {
        return regimen;
    }

    public PredioPersona regimen(Regimen regimen) {
        this.regimen = regimen;
        return this;
    }

    public void setRegimen(Regimen regimen) {
        this.regimen = regimen;
    }

    public Nacionalidad getNacionalidad() {
        return nacionalidad;
    }

    public PredioPersona nacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
        return this;
    }

    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public Persona getPersona() {
        return persona;
    }

    public PredioPersona persona(Persona persona) {
        this.persona = persona;
        return this;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Boolean getPrimerRegistro() {
		return primerRegistro;
	}

	public void setPrimerRegistro(Boolean primerRegistro) {
		this.primerRegistro = primerRegistro;
	}
	
	public Double getSuperficie() {
		return superficie;
	}

	public void setSuperficie(Double superficie) {
		this.superficie = superficie;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PredioPersona predioPersona = (PredioPersona) o;
        if (predioPersona.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), predioPersona.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PredioPersona{" +
            "id=" + getId() +
            ", porcentajeDd='" + getPorcentajeDd() + "'" +
            ", porcentajeUv='" + getPorcentajeUv() + "'" +
            ", superficie='" + getSuperficie() + "'" +
            ", hashFila='" + getHashFila() + "'" +
            ", menorEdad='" + isMenorEdad() + "'" +
            "}";
    }

	public Boolean getExtinto() {
		return extinto;
	}

	public void setExtinto(Boolean extinto) {
		this.extinto = extinto;
	}
}
