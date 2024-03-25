package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A FoliosFracCond.
 */
@Entity
@Table(name = "folios_frac_cond")
public class FoliosFracCond implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "foliosFracCondGenerator")
    @SequenceGenerator(allocationSize = 1, name = "foliosFracCondGenerator", sequenceName="folios_frac_cond_seq")
    private Long id;

    @ManyToOne
    @NotNull
    private Predio predio;

    @ManyToOne(optional = true)
    private Acto acto;

    @Column(name = "procedente", nullable = false)
    private Boolean procedente;

    @Column(name = "no_predio")
    private Integer noPredio;
    
    @ManyToOne
	private PaseFracCond paseFracCond;
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Predio getPredio() {
        return predio;
    }

    public FoliosFracCond predio(Predio predio) {
        this.predio = predio;
        return this;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }

    public Acto getActo() {
        return acto;
    }

    public FoliosFracCond acto(Acto acto) {
        this.acto = acto;
        return this;
    }

    public void setActo(Acto acto) {
        this.acto = acto;
    }

    public Boolean getProcedente() {
        return procedente;
    }

    public FoliosFracCond procedente(Boolean procedente) {
        this.procedente = procedente;
        return this;
    }

    public void setProcedente(Boolean procedente) {
        this.procedente = procedente;
    }

    public Integer getNoPredio() {
        return noPredio;
    }

    public FoliosFracCond noPredio(Integer noPredio) {
        this.noPredio = noPredio;
        return this;
    }

    public void setNoPredio(Integer noPredio) {
        this.noPredio = noPredio;
    }
	
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FoliosFracCond foliosFracCond = (FoliosFracCond) o;
        if (foliosFracCond.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), foliosFracCond.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FolioFracCond{" +
            "id=" + getId() +
            "predio=" + getPredio() +
            "acto=" + getActo() +
            "procedente=" + getProcedente() +
            "}";
    }

	public PaseFracCond getPaseFracCond() {
		return paseFracCond;
	}

	public void setPaseFracCond(PaseFracCond paseFracCond) {
		this.paseFracCond = paseFracCond;
	}
}
