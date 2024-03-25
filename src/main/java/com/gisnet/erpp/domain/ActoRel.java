package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ActoRel.
 */
@Entity
@Table(name = "acto_rel")
public class ActoRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoRelGenerator")
    @SequenceGenerator(allocationSize = 1, name = "actoRelGenerator", sequenceName="acto_rel_seq")
    private Long id;

    @ManyToOne
    private Acto actoSig;
    
    @ManyToOne
    private Acto actoAnt;

    @ManyToOne(optional = false)
    @NotNull
    private Acto acto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public Acto getActoSig() {
		return actoSig;
	}

	public void setActoSig(Acto actoSig) {
		this.actoSig = actoSig;
	}

	public Acto getActo() {
        return acto;
    }

    public ActoRel acto(Acto acto) {
        this.acto = acto;
        return this;
    }

    public void setActo(Acto acto) {
        this.acto = acto;
    }
    
    public Acto getActoAnt() {
		return actoAnt;
	}

	public void setActoAnt(Acto actoAnt) {
		this.actoAnt = actoAnt;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActoRel actoRel = (ActoRel) o;
        if (actoRel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actoRel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActoRel{" +
            "id=" + getId() +
            "}";
    }
}
