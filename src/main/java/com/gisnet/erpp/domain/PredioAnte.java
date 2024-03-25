package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PredioAnte.
 */
@Entity
@Table(name = "predio_ante")
public class PredioAnte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "predioAnteGenerator")
    @SequenceGenerator(allocationSize = 1, name = "predioAnteGenerator", sequenceName="predio_ante_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Antecedente antecedente;

    @ManyToOne(optional = false)
    @NotNull
    private Predio predio;

    transient
    private String foliosNoEncontradosMasiva;
    
    
    public String getFoliosNoEncontradosMasiva() {
		return foliosNoEncontradosMasiva;
	}
    
    
	public void setFoliosNoEncontradosMasiva(String foliosNoEncontradosMasiva) {
		this.foliosNoEncontradosMasiva = foliosNoEncontradosMasiva;
	}
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Antecedente getAntecedente() {
        return antecedente;
    }

    public PredioAnte antecedente(Antecedente antecedente) {
        this.antecedente = antecedente;
        return this;
    }

    public void setAntecedente(Antecedente antecedente) {
        this.antecedente = antecedente;
    }

    public Predio getPredio() {
        return predio;
    }

    public PredioAnte predio(Predio predio) {
        this.predio = predio;
        return this;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PredioAnte predioAnte = (PredioAnte) o;
        if (predioAnte.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), predioAnte.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PredioAnte{" +
            "id=" + getId() +
            "}";
    }
}
