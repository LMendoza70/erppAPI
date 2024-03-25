package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Antecedente.
 */
@Entity
@Table(name = "antecedente")
public class Antecedente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "antecedenteGenerator")
    @SequenceGenerator(allocationSize = 1, name = "antecedenteGenerator", sequenceName="antecedente_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "documento", length = 100)
    private String documento;

    @Size(max = 100)
    @Column(name = "documento_bis", length = 100)
    private String documentoBis;
    
    @Size(max = 200)
    @Column(name = "observaciones", length = 200)
    private String observaciones;

    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    
    @OneToMany(mappedBy = "antecedente")
    @JsonIgnore
    private Set<ActoAnte> actoAntesParaAntecedentes = new HashSet<>();

    @OneToMany(mappedBy = "antecedente")
    @JsonIgnore
    private Set<PredioAnte> predioAntesParaAntecedentes = new HashSet<>();
    
    @OneToMany(mappedBy = "antecedente")
    @JsonIgnore
    private Set<MuebleAnte> muebleAntesParaAntecedentes = new HashSet<>();
    
    @OneToMany(mappedBy = "antecedente")
    @JsonIgnore
    private Set<AuxiliarAnte> auxiliarAntesParaAntecedentes = new HashSet<>();
    
    @OneToMany(mappedBy = "antecedente")
    @JsonIgnore
    private Set<PjAnte> pjAntesParaAntecedentes = new HashSet<>();

    @ManyToOne
    private Libro libro;
         
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public Antecedente documento(String documento) {
        this.documento = documento;
        return this;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
    
    public String getDocumentoBis() {
        return documentoBis;
    }

    public Antecedente documentoBis(String documentoBis) {
        this.documentoBis = documentoBis;
        return this;
    }

    public void setDocumentoBis(String documentoBis) {
        this.documentoBis = documentoBis;
    }
    
    public String getObservaciones() {
        return observaciones;
    }

    public Antecedente observaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Set<ActoAnte> getActoAntesParaAntecedentes() {
        return actoAntesParaAntecedentes;
    }

    public Antecedente actoAntesParaAntecedentes(Set<ActoAnte> actoAntes) {
        this.actoAntesParaAntecedentes = actoAntes;
        return this;
    }

    public Antecedente addActoAntesParaAntecedente(ActoAnte actoAnte) {
        this.actoAntesParaAntecedentes.add(actoAnte);
        actoAnte.setAntecedente(this);
        return this;
    }

    public Antecedente removeActoAntesParaAntecedente(ActoAnte actoAnte) {
        this.actoAntesParaAntecedentes.remove(actoAnte);
        actoAnte.setAntecedente(null);
        return this;
    }

    public void setActoAntesParaAntecedentes(Set<ActoAnte> actoAntes) {
        this.actoAntesParaAntecedentes = actoAntes;
    }

    public Set<PredioAnte> getPredioAntesParaAntecedentes() {
        return predioAntesParaAntecedentes;
    }

    public Antecedente predioAntesParaAntecedentes(Set<PredioAnte> predioAntes) {
        this.predioAntesParaAntecedentes = predioAntes;
        return this;
    }

    public Antecedente addPredioAntesParaAntecedente(PredioAnte predioAnte) {
        this.predioAntesParaAntecedentes.add(predioAnte);
        predioAnte.setAntecedente(this);
        return this;
    }

    public Antecedente removePredioAntesParaAntecedente(PredioAnte predioAnte) {
        this.predioAntesParaAntecedentes.remove(predioAnte);
        predioAnte.setAntecedente(null);
        return this;
    }

    public void setPredioAntesParaAntecedentes(Set<PredioAnte> predioAntes) {
        this.predioAntesParaAntecedentes = predioAntes;
    }

   
    public Libro getLibro() {
        return libro;
    }

    public Antecedente libro(Libro libro) {
        this.libro = libro;
        return this;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
   
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Antecedente antecedente = (Antecedente) o;
        if (antecedente.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), antecedente.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Antecedente{" +
            "id=" + getId() +
            ",libro=" + (libro==null?"":libro.descripcion()) +
            ", documento='" + getDocumento() + "'" +
            ", documentoBis='" + getDocumentoBis() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
    
    public String getDescripcion(){
    	return toString();
    }


	public Set<MuebleAnte> getMuebleAntesParaAntecedentes() {
		return muebleAntesParaAntecedentes;
	}

	public void setMuebleAntesParaAntecedentes(Set<MuebleAnte> muebleAntesParaAntecedentes) {
		this.muebleAntesParaAntecedentes = muebleAntesParaAntecedentes;
	}

	public Set<AuxiliarAnte> getAuxiliarAntesParaAntecedentes() {
		return auxiliarAntesParaAntecedentes;
	}

	public void setAuxiliarAntesParaAntecedentes(Set<AuxiliarAnte> auxiliarAntesParaAntecedentes) {
		this.auxiliarAntesParaAntecedentes = auxiliarAntesParaAntecedentes;
	}

	public Set<PjAnte> getPjAntesParaAntecedentes() {
		return pjAntesParaAntecedentes;
	}

	public void setPjAntesParaAntecedentes(Set<PjAnte> pjAntesParaAntecedentes) {
		this.pjAntesParaAntecedentes = pjAntesParaAntecedentes;
	}
	

	/*public Mueble getMueble() {
		return mueble;
	}

	public void setMueble(Mueble mueble) {
		this.mueble = mueble;
	}*/
        
        
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
}
