package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PredioPersona.
 */
@Entity
@Table(name = "acto_predio_monto")
public class ActoPredioMonto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoPredioMontoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "actoPredioMontoGenerator", sequenceName="acto_predio_monto_seq")
    private Long id;
    
    @ManyToOne(optional = true)
    private Acto acto;

    @Column(name = "monto", precision=15, scale=2)
    private BigDecimal monto;
    
    @Column(name = "porcentaje")
    private Float porcentaje;
    
    @ManyToOne(optional = false)
    @NotNull
    private ActoPredio actoPredio;
    
    @ManyToOne
    private TipoMoneda tipoMoneda;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Acto getActo() {
		return acto;
	}

	public void setActo(Acto acto) {
		this.acto = acto;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public Float getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Float porcentaje) {
		this.porcentaje = porcentaje;
	}

	public ActoPredio getActoPredio() {
		return actoPredio;
	}

	public void setActoPredio(ActoPredio actoPredio) {
		this.actoPredio = actoPredio;
	}
		
	public TipoMoneda getTipoMoneda() {
		return tipoMoneda;
	}

	public void setTipoMoneda(TipoMoneda tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActoPredioMonto actoPredioMonto = (ActoPredioMonto) o;
        if (actoPredioMonto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actoPredioMonto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "actoPredioMonto{" +
            "id=" + getId() +
            ", porcentaje='" + getPorcentaje() + "'" +
            ", monto='" + getMonto() + "'"  +
            "}";
    }
}
