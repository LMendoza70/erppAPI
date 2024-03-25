package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A PredioExtinto.
 */
@Entity
@Table(name = "predio_extinto")
public class PredioExtinto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "predioExtintoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "predioExtintoGenerator", sequenceName="predio_ext_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;

    @ManyToOne(optional = false)
    @NotNull
    private Predio predio;
    
    @Size(max = 2000)
    @Column(name = "fundamento", length = 2000)
    private String fundamento;
    
    @Column(name = "fecha")
    private Date fecha;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


	public Predio getPredio() {
        return predio;
    }

    public PredioExtinto predio(Predio predio) {
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
        PredioExtinto predioExtinto = (PredioExtinto) o;
        if (predioExtinto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), predioExtinto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PredioExtinto{" +
            "id=" + getId() +
            "}";
    }

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getFundamento() {
		return fundamento;
	}

	public void setFundamento(String fundamento) {
		this.fundamento = fundamento;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
