package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A ParametroCotizadorCosto.
 */
@Entity
@Table(name = "parametro_cotizador_costo")
public class ParametroCotizadorCosto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parametroCotizadorCostoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "parametroCotizadorCostoGenerator", sequenceName="parametro_cotizador_costo_seq")
    private Long id;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "costo", precision=10, scale=2)
    private BigDecimal costo;

    @ManyToOne(optional = false)
    @NotNull
    private ParametroCotizador parametroCotizador;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public ParametroCotizadorCosto anio(Integer anio) {
        this.anio = anio;
        return this;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public ParametroCotizadorCosto costo(BigDecimal costo) {
        this.costo = costo;
        return this;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public ParametroCotizador getParametroCotizador() {
        return parametroCotizador;
    }

    public ParametroCotizadorCosto parametroCotizador(ParametroCotizador parametroCotizador) {
        this.parametroCotizador = parametroCotizador;
        return this;
    }

    public void setParametroCotizador(ParametroCotizador parametroCotizador) {
        this.parametroCotizador = parametroCotizador;
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParametroCotizadorCosto parametroCotizadorCosto = (ParametroCotizadorCosto) o;
        if (parametroCotizadorCosto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), parametroCotizadorCosto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ParametroCotizadorCosto{" +
            "id=" + getId() +
            ", anio='" + getAnio() + "'" +
            ", costo='" + getCosto() + "'" +
            "}";
    }
}
