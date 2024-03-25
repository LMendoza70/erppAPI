package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Pases.
 */
@Entity
@Table(name = "pases")
public class Pases implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pasesGenerator")
    @SequenceGenerator(allocationSize = 1, name = "pasesGenerator", sequenceName="pases_seq")
    private Long id;

    @Column(name = "superficie", length = 20)
    private String superficie;

    @ManyToOne
    private Predio predioOrigen;

    @ManyToOne
    private Predio predioSegre;

    @ManyToOne
    private UnidadMedida unidadMedida;

    @Column(name = "fraccion", length = 4)
    private Integer fraccion;
    
    @ManyToOne
    private Acto acto;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSuperficie() {
        return superficie;
    }

    public void setSuperficie(String superficie) {
        this.superficie = superficie;
    }

    public Integer getFraccion() {
        return fraccion;
    }

    public void setFraccion(Integer fraccion) {
        this.fraccion = fraccion;
    }

    public Predio getPredioOrigen() {
        return predioOrigen;
    }

    public void setPredioOrigen(Predio predioOrigen) {
        this.predioOrigen = predioOrigen;
    }

    public Predio getPredioSegre() {
        return predioSegre;
    }

    public void setPredioSegre(Predio predioSegre) {
        this.predioSegre = predioSegre;
    }

    public UnidadMedida getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(UnidadMedida unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pases pases = (Pases) o;
        if (pases.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pases.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pases{" +
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
