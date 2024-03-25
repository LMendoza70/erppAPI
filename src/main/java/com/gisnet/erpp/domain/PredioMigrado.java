package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PredioMigrado.
 */
@Entity
@Table(name = "predio_migrado")
public class PredioMigrado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "predioMigradoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "predioMigradoGenerator", sequenceName="predio_migrado_seq")
    private Long id;

    @Size(max = 4000)
    @Column(name = "direccion_migrada", length = 4000)
    private String direccionMigrada;

    @Size(max = 30000)
    @Column(name = "colindancias_migr", length = 30000)
    private String colindanciasMigr;

    @Size(max = 4000)
    @Column(name = "antecedentes_migr", length = 4000)
    private String antecedentesMigr;

    @ManyToOne
    private Predio predio;
    
    @ManyToOne
    private TipoFolio tipoFolio;
    
    @ManyToOne
    private PersonaJuridica personaJuridica;
    
    @ManyToOne
    private FolioSeccionAuxiliar folioSeccionAuxiliar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccionMigrada() {
        return direccionMigrada;
    }

    public PredioMigrado direccionMigrada(String direccionMigrada) {
        this.direccionMigrada = direccionMigrada;
        return this;
    }

    public void setDireccionMigrada(String direccionMigrada) {
        this.direccionMigrada = direccionMigrada;
    }

    public String getColindanciasMigr() {
        return colindanciasMigr;
    }

    public PredioMigrado colindanciasMigr(String colindanciasMigr) {
        this.colindanciasMigr = colindanciasMigr;
        return this;
    }

    public void setColindanciasMigr(String colindanciasMigr) {
        this.colindanciasMigr = colindanciasMigr;
    }

    public String getAntecedentesMigr() {
        return antecedentesMigr;
    }

    public PredioMigrado antecedentesMigr(String antecedentesMigr) {
        this.antecedentesMigr = antecedentesMigr;
        return this;
    }

    public void setAntecedentesMigr(String antecedentesMigr) {
        this.antecedentesMigr = antecedentesMigr;
    }

    public Predio getPredio() {
        return predio;
    }

    public PredioMigrado predio(Predio predio) {
        this.predio = predio;
        return this;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }
    
    public TipoFolio getTipoFolio(){
        return tipoFolio;
    }
    
    public void setTipoFolio(TipoFolio tipoFolio){
        this.tipoFolio = tipoFolio;
    }
    
    public PersonaJuridica getPersonaJuridica(){
        return personaJuridica;
    }
    
    public void setPersonaJuridica(PersonaJuridica personaJuridica){
        this.personaJuridica = personaJuridica;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PredioMigrado predioMigrado = (PredioMigrado) o;
        if (predioMigrado.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), predioMigrado.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PredioMigrado{" +
            "id=" + getId() +
            ", direccionMigrada='" + getDireccionMigrada() + "'" +
            ", colindanciasMigr='" + getColindanciasMigr() + "'" +
            ", antecedentesMigr='" + getAntecedentesMigr() + "'" +
            "}";
    }

	public FolioSeccionAuxiliar getFolioSeccionAuxiliar() {
		return folioSeccionAuxiliar;
	}

	public void setFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar) {
		this.folioSeccionAuxiliar = folioSeccionAuxiliar;
	}
}
