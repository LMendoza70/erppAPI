package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PredioRel.
 */
@Entity
@Table(name = "predio_rel")
public class PredioRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "predioRelGenerator")
    @SequenceGenerator(allocationSize = 1, name = "predioRelGenerator", sequenceName="predio_rel_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Predio predioSig;

    @ManyToOne(optional = false)
    @NotNull
    private Predio predio;
    
    
    @ManyToOne
    private Acto acto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public Predio getPredioSig() {
		return predioSig;
	}

	public void setPredioSig(Predio predioSig) {
		this.predioSig = predioSig;
	}

	public Predio getPredio() {
        return predio;
    }

    public PredioRel predio(Predio predio) {
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
        PredioRel predioRel = (PredioRel) o;
        if (predioRel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), predioRel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PredioRel{" +
            "id=" + getId() +
            "}";
    }

	public Acto getActo() {
		return acto;
	}

	public void setActo(Acto acto) {
		this.acto = acto;
	}
}
