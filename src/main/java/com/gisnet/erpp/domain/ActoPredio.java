package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ActoPredio.
 */
@Entity
@Table(name = "acto_predio")
public class ActoPredio implements Serializable, Comparable<ActoPredio> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoPredioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "actoPredioGenerator", sequenceName="acto_predio_seq")
    private Long id;

    @OneToMany(mappedBy = "actoPredio")
    @JsonIgnore
    private Set<ActoPredioCampo> actoPredioCamposParaActoPredios = new HashSet<>();

    @OneToMany(mappedBy = "actoPredio")
    @JsonIgnore
    private Set<PredioPersona> predioPersonasParaActoPredios = new HashSet<>();
    
    @OneToMany(mappedBy = "actoPredio")
    @JsonIgnore
    private Set<ActoPredioMonto> actoPredioMontosParaActoPredios = new HashSet<>();
    
    @OneToMany(mappedBy = "actoPredio")
    @JsonIgnore
    private Set<PjPersona> personaJuridicaPersonasParaActoPredios = new HashSet<>();
    
    @OneToMany(mappedBy = "actoPredio")
    @JsonIgnore
    private Set<AuxiliarPersona> folioSeccionAuxiliarPersonasParaActoPredios = new HashSet<>();
    
    @OneToMany(mappedBy = "actoPredio")
    @JsonIgnore
    private Set<MueblePersona> mueblePersonasParaActoPredios = new HashSet<>();

    @ManyToOne
    private Predio predio;

    @ManyToOne(optional = false)
    @NotNull
    private Acto acto;

    @ManyToOne(optional = false)
    @NotNull
    private TipoEntrada tipoEntrada;
    
    @Column(name = "version", length = 3, nullable = false)
    private Integer version;
	
    @ManyToOne
    private TipoFolio tipoFolio;
	
    @ManyToOne
    private Mueble mueble;
    
    @ManyToOne
    private PersonaJuridica personaJuridica;
    
    @ManyToOne
    private FolioSeccionAuxiliar folioSeccionAuxiliar;
	
    @Column(name="id_entrada")
    private Long id_entrada;
    
    @Column(name="id_folio_real")
    private Long id_folio_real;
    
    @Column(name = "sin_id_folio_real")
    private Boolean sin_id_folio_real;
    
    
    public Long getId_folio_real() {
		return id_folio_real;
	}

	public void setId_folio_real(Long id_folio_real) {
		this.id_folio_real = id_folio_real;
	}
	

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ActoPredioCampo> getActoPredioCamposParaActoPredios() {
        return actoPredioCamposParaActoPredios;
    }

    public ActoPredio actoPredioCamposParaActoPredios(Set<ActoPredioCampo> actoPredioCampos) {
        this.actoPredioCamposParaActoPredios = actoPredioCampos;
        return this;
    }

    public ActoPredio addActoPredioCamposParaActoPredio(ActoPredioCampo actoPredioCampo) {
        this.actoPredioCamposParaActoPredios.add(actoPredioCampo);
        actoPredioCampo.setActoPredio(this);
        return this;
    }

    public ActoPredio removeActoPredioCamposParaActoPredio(ActoPredioCampo actoPredioCampo) {
        this.actoPredioCamposParaActoPredios.remove(actoPredioCampo);
        actoPredioCampo.setActoPredio(null);
        return this;
    }

    public void setActoPredioCamposParaActoPredios(Set<ActoPredioCampo> actoPredioCampos) {
        this.actoPredioCamposParaActoPredios = actoPredioCampos;
    }

    public Set<PredioPersona> getPredioPersonasParaActoPredios() {
        return predioPersonasParaActoPredios;
    }

    	
    public ActoPredio predioPersonasParaActoPredios(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaActoPredios = predioPersonas;
        return this;
    }

    public ActoPredio addPredioPersonasParaActoPredio(PredioPersona predioPersona) {
        this.predioPersonasParaActoPredios.add(predioPersona);
        predioPersona.setActoPredio(this);
        return this;
    }

    public ActoPredio removePredioPersonasParaActoPredio(PredioPersona predioPersona) {
        this.predioPersonasParaActoPredios.remove(predioPersona);
        predioPersona.setActoPredio(null);
        return this;
    }

    public void setPredioPersonasParaActoPredios(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaActoPredios = predioPersonas;
    }

    public Set<PjPersona> getPersonaJuridicaPersonasParaActoPredios() {
        return personaJuridicaPersonasParaActoPredios;
    }

    
    public Set<AuxiliarPersona> getFolioSeccionAuxiliarPersonasParaActoPredios() {
        return folioSeccionAuxiliarPersonasParaActoPredios;
    }

    public Set<MueblePersona> getMueblePersonasParaActoPredios() {
        return mueblePersonasParaActoPredios;
    }
    
    
    public Set<ActoPredioMonto> getActoPredioMontosParaActoPredios() {
		return actoPredioMontosParaActoPredios;
	}

	public void setActoPredioMontosParaActoPredios(Set<ActoPredioMonto> actoPredioMontosParaActoPredios) {
		this.actoPredioMontosParaActoPredios = actoPredioMontosParaActoPredios;
	}

	public Predio getPredio() {
        return predio;
    }

    public ActoPredio predio(Predio predio) {
        this.predio = predio;
        return this;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }

    public Acto getActo() {
        return acto;
    }

    public ActoPredio acto(Acto acto) {
        this.acto = acto;
        return this;
    }

    public void setActo(Acto acto) {
        this.acto = acto;
    }

    public TipoEntrada getTipoEntrada() {
        return tipoEntrada;
    }

    public ActoPredio tipoEntrada(TipoEntrada tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
        return this;
    }

    public void setTipoEntrada(TipoEntrada tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }
    
    public Integer getVersion() {
        return version;
    }

    public ActoPredio version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
	
    public TipoFolio getTipoFolio(){
    	return tipoFolio;
    }
	
    public void setTipoFolio(TipoFolio tipoFolio){
    	this.tipoFolio = tipoFolio;
    }


    public Mueble getMueble(){
    	return mueble;
    }
	
    public void setMueble(Mueble mueble){
    	this.mueble = mueble;
    }
	

    public PersonaJuridica getPersonaJuridica(){
    	return personaJuridica;
    }
	
    public void setPersonaJuridica(PersonaJuridica personaJuridica){
    	this.personaJuridica = personaJuridica;
    }
	
    
    public FolioSeccionAuxiliar getFolioSeccionAuxiliar(){
    	return folioSeccionAuxiliar;
    }
	
    public void setFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar){
    	this.folioSeccionAuxiliar = folioSeccionAuxiliar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActoPredio actoPredio = (ActoPredio) o;
        if (actoPredio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actoPredio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActoPredio{" +
            "id=" + getId() +
            // "actoPrediosParaActoPredioEntradas=" + getActoPrediosParaActoPredioEntradas() +
            // "actoPredioCamposParaActoPredios=" + getActoPredioCamposParaActoPredios() +
            // "predioPersonasParaActoPredios=" + getPredioPersonasParaActoPredios() +
            // "actoPredioEntrada=" + getActoPredioEntrada() +
            "predio=" + getPredio() +
            "acto=" + getActo() +
            "tipoEntrada=" + getTipoEntrada() +
            "version=" + getVersion() +
            "}";
    }

	@Override
	public int compareTo(ActoPredio actoPredio) {
		return actoPredio.getVersion().compareTo(this.version);
	}
}
