package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A ServicioCosto.
 */
@Entity
@Table(name = "servicio_costo")
public class ServicioCosto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "servicioCostoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "servicioCostoGenerator", sequenceName="servicio_costo_seq")
    private Long id;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "costo", precision=10, scale=2)
    private BigDecimal costo;

    @Size(max = 100)
    @Column(name = "formula", length = 100)
    private String formula;

    @Column(name = "aplica_rango")
    private Boolean aplicaRango;

    @Column(name = "valor_pecunario")
    private Boolean valorPecunario;

    @ManyToOne(optional = false)
    @NotNull
    private Servicio servicio;

    @ManyToOne
    private TipoActo tipoActo;

    @ManyToOne
    private ClasifActo clasifActo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public ServicioCosto anio(Integer anio) {
        this.anio = anio;
        return this;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public ServicioCosto costo(BigDecimal costo) {
        this.costo = costo;
        return this;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public String getFormula() {
        return formula;
    }

    public ServicioCosto formula(String formula) {
        this.formula = formula;
        return this;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Boolean isAplicaRango() {
        return aplicaRango;
    }

    public ServicioCosto aplicaRango(Boolean aplicaRango) {
        this.aplicaRango = aplicaRango;
        return this;
    }

    public void setAplicaRango(Boolean aplicaRango) {
        this.aplicaRango = aplicaRango;
    }

    public Boolean isValorPecunario() {
        return valorPecunario;
    }

    public ServicioCosto valorPecunario(Boolean valorPecunario) {
        this.valorPecunario = valorPecunario;
        return this;
    }

    public void setValorPecunario(Boolean valorPecunario) {
        this.valorPecunario = valorPecunario;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public ServicioCosto servicio(Servicio servicio) {
        this.servicio = servicio;
        return this;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public TipoActo getTipoActo() {
        return tipoActo;
    }

    public ServicioCosto tipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
        return this;
    }

    public void setTipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
    }

    public ClasifActo getClasifActo() {
        return clasifActo;
    }

    public ServicioCosto clasifActo(ClasifActo clasifActo) {
        this.clasifActo = clasifActo;
        return this;
    }

    public void setClasifActo(ClasifActo clasifActo) {
        this.clasifActo = clasifActo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServicioCosto servicioCosto = (ServicioCosto) o;
        if (servicioCosto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), servicioCosto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServicioCosto{" +
            "id=" + getId() +
            ", anio='" + getAnio() + "'" +
            ", costo='" + getCosto() + "'" +
            ", formula='" + getFormula() + "'" +
            ", aplicaRango='" + isAplicaRango() + "'" +
            ", valorPecunario='" + isValorPecunario() + "'" +
            "}";
    }
}
